package com.project.placement_management_app.service.placement;

import com.project.placement_management_app.dto.request.FilterPlacementsRequestDto;
import com.project.placement_management_app.dto.request.PlacementDto;
import com.project.placement_management_app.dto.response.PostcodesIOApiResponseDto;
import com.project.placement_management_app.exception.EmailMessagingException;
import com.project.placement_management_app.exception.PreConditionFailedException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.PlacementRepository;
import com.project.placement_management_app.service.email.EmailService;
import com.project.placement_management_app.service.user.UserService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlacementServiceImpl implements PlacementService {

    @Autowired
    private UserService userService;
    @Autowired
    private PlacementRepository placementRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${email.notifications.enable}")
    private boolean isEmailNotificationEnable;
    private static final Logger LOG = LoggerFactory.getLogger(PlacementServiceImpl.class);

    public Placement addPlacement(PlacementAuthorizationRequest placementAuthorizationRequest, ProviderForm providerForm){

        PostcodesIOApiResponseDto postcodesIOApiResponseDto = getLatLngFromPostcode(providerForm.getPlacementProvider().getPostcode());

        Placement placement = Placement.buildPlacementWith()
                .requestId(placementAuthorizationRequest.getId())
                .roleTitle(placementAuthorizationRequest.getPlacementRole().getTitle())
                .startDate(placementAuthorizationRequest.getPlacementRole().getStartDate())
                .endDate(placementAuthorizationRequest.getPlacementRole().getEndDate())
                .student(Student.buildStudentWith()
                        .firstName(placementAuthorizationRequest.getStudent().getFirstName())
                        .lastName(placementAuthorizationRequest.getStudent().getLastName())
                        .emailId(placementAuthorizationRequest.getStudent().getEmailId())
                        .studentNumber(placementAuthorizationRequest.getStudent().getStudentNumber())
                        .course(placementAuthorizationRequest.getStudent().getCourse())
                        .school(placementAuthorizationRequest.getStudent().getSchool())
                        .telephone(placementAuthorizationRequest.getStudent().getTelephone())
                        .academicYear(placementAuthorizationRequest.getStudent().getAcademicYear())
                        .build())
                .placementProvider(PlacementProvider.buildPlacementProviderWith()
                        .name(providerForm.getPlacementProvider().getName())
                        .webAddress(providerForm.getPlacementProvider().getWebAddress())
                        .address(providerForm.getPlacementProvider().getAddress())
                        .postcode(providerForm.getPlacementProvider().getPostcode())
                        .longitude(postcodesIOApiResponseDto.getLongitude())
                        .latitude(postcodesIOApiResponseDto.getLatitude())
                        .contactName(providerForm.getPlacementProvider().getContactName())
                        .contactJobTitle(providerForm.getPlacementProvider().getContactJobTitle())
                        .contactEmail(providerForm.getPlacementProvider().getContactEmail())
                        .telephone(providerForm.getPlacementProvider().getTelephone())
                        .build())
                .build();
        return placementRepository.save(placement);
    }

    public List<Placement> getPlacementsByUserId(String userId) {
        LOG.info("Get user details by provided userId");
        User user = userService.getUserById(userId);
        if (user == null){
            LOG.error("user is Invalid");
            throw new PreConditionFailedException("user is Invalid");
        }

        List<Placement> placementList = new ArrayList<Placement>();
        if(user.getUserRole() == Role.STUDENT){
            LOG.info("Fetching the placements of the student");
            placementList = placementRepository.findPlacementsByStudentEmailID(user.getEmailId());
        }else if (user.getUserRole() == Role.TUTOR){
            LOG.info("Fetching the placements that belongs to the school of tutor");
            placementList = placementRepository.findPlacementsBySchool(user.getSchool());
        }

        if(placementList.isEmpty()){
            LOG.error("No placements found");
            throw new ResourceNotFoundException("No placements found");
        }

        return placementList;
    }

    public Placement getPlacementById(String placementId) {
        return placementRepository.findPlacementByID(placementId);
    }

    public Placement updatePlacement(PlacementDto placementDto) {

        Placement placement = placementRepository.findPlacementByID(placementDto.getId());

        List<String> updatesList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<String> toEmails = new ArrayList<String>();
        toEmails.add(placement.getPlacementProvider().getContactEmail());

        if(!placement.getStartDate().equals(placementDto.getStartDate())){
            updatesList.add("Placement Start Date is updated to " +
                    dateFormat.format(placementDto.getStartDate()).toString() + " from " +
                    dateFormat.format(placement.getStartDate()).toString());
            placement.setStartDate(placementDto.getStartDate());
        }
        if(!placement.getEndDate().equals(placementDto.getEndDate())){
            updatesList.add("Placement End Date is updated to " +
                    dateFormat.format(placementDto.getEndDate()).toString() + " from " +
                    dateFormat.format(placement.getEndDate()).toString());
            placement.setEndDate(placementDto.getEndDate());
        }
        if(!placement.getPlacementProvider().getContactName().equals(placementDto.getPlacementProvider().getContactName())){
            updatesList.add("Provider Contact Name is updated to " +
                    placementDto.getPlacementProvider().getContactName() + " from " +
                    placement.getPlacementProvider().getContactName());
            placement.getPlacementProvider().setContactName(placementDto.getPlacementProvider().getContactName());
        }
        if(!placement.getPlacementProvider().getContactJobTitle().equals(placementDto.getPlacementProvider().getContactJobTitle())){
            updatesList.add("Provider Contact Job Title is updated to " +
                    placementDto.getPlacementProvider().getContactJobTitle() + " from " +
                    placement.getPlacementProvider().getContactJobTitle());
            placement.getPlacementProvider().setContactJobTitle(placementDto.getPlacementProvider().getContactJobTitle());
        }
        if(!placement.getPlacementProvider().getContactEmail().equals(placementDto.getPlacementProvider().getContactEmail())){
            updatesList.add("Provider Contact Email is updated to " +
                    placementDto.getPlacementProvider().getContactEmail() + " from " +
                    placement.getPlacementProvider().getContactEmail());
            placement.getPlacementProvider().setContactEmail(placementDto.getPlacementProvider().getContactEmail());
        }
        if(!placement.getPlacementProvider().getTelephone().equals(placementDto.getPlacementProvider().getTelephone())){
            updatesList.add("Provider Contact Telephone is updated to " +
                    placementDto.getPlacementProvider().getTelephone() + " from " +
                    placement.getPlacementProvider().getTelephone());
            placement.getPlacementProvider().setTelephone(placementDto.getPlacementProvider().getTelephone());
        }

        if(!updatesList.isEmpty()){
            placementRepository.save(placement);

            if(isEmailNotificationEnable){
                EmailTemplate emailTemplate = emailService.getEmailTemplate("UpdatedPlacementNotification");

                Map<String, String> emailParameters = new HashMap<String, String>();
                emailParameters.put("studentName",
                        placement.getStudent().getFirstName() + " " + placement.getStudent().getLastName());
                String subject = emailService.createSubject(emailTemplate.getSubject(), emailParameters);

                emailParameters.put("updatedPlacementDetails",String.join("<br>",updatesList));
                String emailContext = emailService.createEmailContext(emailTemplate.getEmailContent(), emailParameters);

                List<User> userList = userService.getUsersBySchoolAndRole(placement.getStudent().getSchool(),Role.TUTOR);

                if(userList != null && !userList.isEmpty()){
                    toEmails.addAll(userList.stream().map(user -> user.getEmailId()).collect(Collectors.toList()));
                }
                try {
                    emailService.sendEmail(toEmails.toArray(new String[toEmails.size()]),
                            subject,
                            emailContext,
                            emailTemplate.isContentHTML(),
                            new String[]{});
                } catch (MessagingException e) {
                    throw new EmailMessagingException("Sending email notification is failed");
                }
            }
        }
        return placement;
    }

    public List<Placement> getFilteredPlacements(FilterPlacementsRequestDto filterPlacementsRequestDto){
        Query query = new Query();

        if(filterPlacementsRequestDto.getProviderName() != null && !filterPlacementsRequestDto.getProviderName().isBlank()){
            query.addCriteria(
                    Criteria.where("placementProvider.name").is(filterPlacementsRequestDto.getProviderName()));
        }
        if(filterPlacementsRequestDto.getStudentCourse() != null && !filterPlacementsRequestDto.getStudentCourse().isBlank()){
            query.addCriteria(
                    Criteria.where("student.course").is(filterPlacementsRequestDto.getStudentCourse()));
        }
        if(filterPlacementsRequestDto.getStudentName() != null && !filterPlacementsRequestDto.getStudentName().isBlank()){
            String[] fullName = filterPlacementsRequestDto.getStudentName().split(" ");
            query.addCriteria(Criteria.where("student.firstName").is(fullName[0]));
            if(fullName.length > 1){
                query.addCriteria(Criteria.where("student.lastName").is(fullName[fullName.length -1]));
            }
        }

        List<Placement> placementList = mongoTemplate.find(query, Placement.class);

        if(placementList.isEmpty()){
            LOG.error("No placements found based on given filter details");
            throw new ResourceNotFoundException("No placements found based on given filter details");
        }

        return placementList;
    }

    public List<Placement> getPlacementsBySchool(String schoolName) { return placementRepository.findPlacementsBySchool(schoolName); }

    private PostcodesIOApiResponseDto getLatLngFromPostcode(String postcode){

        PostcodesIOApiResponseDto postcodesIOApiResponseDto = PostcodesIOApiResponseDto
                .buildPostcodesIOApiResponseDtoWith().build();
        try {
            LinkedHashMap<String, LinkedHashMap> response = restTemplate.getForObject("https://api.postcodes.io/postcodes/" + postcode, LinkedHashMap.class);

            LinkedHashMap<String, Object> value =  response.get("result");
            postcodesIOApiResponseDto.setLongitude((Double) value.get("longitude"));
            postcodesIOApiResponseDto.setLatitude((Double) value.get("latitude"));

            return postcodesIOApiResponseDto;

        }catch (HttpClientErrorException e){
            return postcodesIOApiResponseDto;
        }catch (Exception e){
            return postcodesIOApiResponseDto;
        }
    }
}
