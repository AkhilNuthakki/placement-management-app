package com.project.placement_management_app.service.placementVisit;

import com.project.placement_management_app.dto.request.PlacementVisitDto;
import com.project.placement_management_app.dto.request.PlacementVisitSlotDto;
import com.project.placement_management_app.exception.EmailMessagingException;
import com.project.placement_management_app.exception.PreConditionFailedException;
import com.project.placement_management_app.exception.ResourceNotFoundException;
import com.project.placement_management_app.model.*;
import com.project.placement_management_app.repository.PlacementVisitRepository;
import com.project.placement_management_app.repository.PlacementVisitSlotRepository;
import com.project.placement_management_app.service.email.EmailService;
import com.project.placement_management_app.service.placement.PlacementService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlacementVisitServiceImpl implements PlacementVisitService {

    @Autowired
    private PlacementVisitRepository placementVisitRepository;
    @Autowired
    private PlacementVisitSlotRepository placementVisitSlotRepository;
    @Autowired
    private PlacementService placementService;
    @Autowired
    private EmailService emailService;
    @Value("${email.notifications.enable}")
    private boolean isEmailNotificationEnable;

    private static final int EARTH_RADIUS = 6371;

    private static final Logger LOG = LoggerFactory.getLogger(PlacementVisitServiceImpl.class);

    public List<VisitingSlot> getAvailablePlacementVisitSlots(String schoolName) {

        PlacementVisitSlot placementVisitSlot = placementVisitSlotRepository.findPlacementVisitSlotBySchool(schoolName);

        List<VisitingSlot> visitingSlotsList = placementVisitSlot.getSlots().stream().filter(visitingSlot -> (visitingSlot.getPlacementVisitIds().isEmpty())).toList();

        if(visitingSlotsList.isEmpty()){
            LOG.error("No placement visiting slots available");
            throw new ResourceNotFoundException("No placement visiting slots available");
        }
        return visitingSlotsList;
    }

    public PlacementVisit schedulePlacementVisit(PlacementVisitDto placementVisitDto) {

        List<PlacementVisit> placementVisitList = placementVisitRepository.findPlacementVisitBySlotTime(placementVisitDto.getStartTime());

        if(!placementVisitList.isEmpty()){
            LOG.error("No placement visit slots available for the provided time slot");
            throw new PreConditionFailedException("No placement visit slots available for the provided time slot");
        }

        PlacementVisit placementVisit;
        Date startTime = null;

        if( placementVisitDto.getId() != null && !placementVisitDto.getId().isBlank()) {
            placementVisit = placementVisitRepository.findPlacementVisitById(placementVisitDto.getId());

            if(!placementVisit.getStartTime().equals(placementVisitDto.getStartTime())){
                startTime = placementVisit.getStartTime();
                placementVisit.setStartTime(placementVisitDto.getStartTime());
                placementVisit.setEndTime(placementVisitDto.getEndTime());
            }
            if(!placementVisit.getProviderContactName().equals(placementVisitDto.getProviderContactName())){
                placementVisit.setProviderContactName(placementVisitDto.getProviderContactName());
            }
            if(!placementVisit.getProviderContactEmail().equals(placementVisitDto.getProviderContactEmail())){
                placementVisit.setProviderContactEmail(placementVisitDto.getProviderContactEmail());
            }
        }else{
            placementVisit = PlacementVisit.buildPlacementVisitWith()
                    .placementId(placementVisitDto.getPlacementId())
                    .placementVisitType(PlacementVisitType.valueOf(placementVisitDto.getPlacementVisitType().toUpperCase()))
                    .startTime(placementVisitDto.getStartTime())
                    .endTime(placementVisitDto.getEndTime())
                    .studentName(placementVisitDto.getStudentName())
                    .studentEmail(placementVisitDto.getStudentEmail())
                    .school(placementVisitDto.getSchool())
                    .providerContactName(placementVisitDto.getProviderContactName())
                    .providerContactEmail(placementVisitDto.getProviderContactEmail())
                    .build();
        }

        placementVisitRepository.save(placementVisit);

        updateScheduledPlacementVisitIdInSlots(placementVisitDto.getSchool(), placementVisit, startTime);

        if(isEmailNotificationEnable){
            EmailTemplate emailTemplate = emailService.getEmailTemplate("PlacementVisitScheduled");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

            Map<String, String> emailParameters = new HashMap<String, String>();
            emailParameters.put("providerName", placementVisit.getProviderContactName());
            emailParameters.put("studentName", placementVisit.getStudentName());
            emailParameters.put("startTime", dateFormat.format(placementVisit.getStartTime()));
            String subject = emailService.createSubject(emailTemplate.getSubject(), emailParameters);
            String emailContext = emailService.createEmailContext(emailTemplate.getEmailContent(), emailParameters);

            String[] toEmails = new String[]{placementVisit.getStudentEmail(), placementVisit.getProviderContactEmail()};

            try {
                emailService.sendCalendarInvite(UUID.randomUUID().toString(),
                        toEmails,
                        subject,
                        emailContext,
                        emailTemplate.isContentHTML(),
                        placementVisit.getStartTime(),
                        placementVisit.getEndTime());
            } catch (MessagingException e) {
                throw new EmailMessagingException("Sending email notification is failed");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return placementVisit;
    }

    public PlacementVisitSlot updateScheduledPlacementVisitIdInSlots(String schoolName, PlacementVisit placementVisit, Date oldVisit){
        PlacementVisitSlot placementVisitSlot = placementVisitSlotRepository.findPlacementVisitSlotBySchool(schoolName);

        for (VisitingSlot visitingSlot : placementVisitSlot.getSlots()){
            if(oldVisit != null){
                if(visitingSlot.getStartTime().equals(oldVisit)) {
                    visitingSlot.getPlacementVisitIds().remove(placementVisit.getId());
                }
            }
            if(visitingSlot.getStartTime().equals(placementVisit.getStartTime())) {
                if(!visitingSlot.getPlacementVisitIds().contains(placementVisit.getId())) {
                    visitingSlot.getPlacementVisitIds().add(placementVisit.getId());
                }
            }
        }
        return placementVisitSlotRepository.save(placementVisitSlot);
    }

    public PlacementVisitSlot updateAvailablePlacementVisitSlots(PlacementVisitSlotDto placementVisitSlotDto) {

        PlacementVisitSlot placementVisitSlot = placementVisitSlotRepository.findPlacementVisitSlotBySchool(placementVisitSlotDto.getSchool());

        if(placementVisitSlot == null){
            placementVisitSlot =  placementVisitSlotRepository.save(PlacementVisitSlot.buildPlacementVisitSlotWith()
                     .academicYear(placementVisitSlotDto.getAcademicYear())
                     .school(placementVisitSlotDto.getSchool())
                     .slots(placementVisitSlotDto.getSlots())
                     .build());
        }else{
            placementVisitSlot.setSlots(placementVisitSlotDto.getSlots());
            placementVisitSlot =  placementVisitSlotRepository.save(placementVisitSlot);
        }

        if (isEmailNotificationEnable){
            List<Placement> placementList = placementService.getPlacementsBySchool(placementVisitSlot.getSchool());

            if(!placementList.isEmpty()){
                EmailTemplate emailTemplate = emailService.getEmailTemplate("PlacementVisitSlotAvailabilityNotification");

                String subject = emailService.createSubject(emailTemplate.getSubject(), new HashMap<String, String>());
                String emailContext = emailService.createEmailContext(emailTemplate.getEmailContent(), new HashMap<String, String>());

                String[] toEmails = placementList.stream().map(placement -> placement.getStudent().getEmailId()).collect(Collectors.toList()).toArray(new String[placementList.size()]);

                try {
                    emailService.sendEmail(toEmails,
                            subject,
                            emailContext,
                            emailTemplate.isContentHTML(),
                            new String[]{});
                } catch (MessagingException e) {
                    throw new EmailMessagingException("Sending email notification is failed");
                }
            }
        }

        return placementVisitSlot;

    }

    public PlacementVisitSlot getPlacementVisitSlots(String schoolName){

        PlacementVisitSlot placementVisitSlot = placementVisitSlotRepository.findPlacementVisitSlotBySchool(schoolName);

        if(placementVisitSlot == null){
            LOG.error("No placement visit slots found for the school");
            throw new ResourceNotFoundException("No placement visit slots found for the school");
        }

        return placementVisitSlot;
    }

    public List<PlacementVisit> getPlacementVisitsByPlacementId(String placementId){

        List<PlacementVisit> placementVisitList = placementVisitRepository.findPlacementVisitByPlacementId(placementId);

        if(placementVisitList.isEmpty()){
            LOG.error("No placement visits found");
            throw new ResourceNotFoundException("No placement visits found");
        }

        return placementVisitList;
    }

    public PlacementVisit getPlacementVisitById(String placementVisitId) {
        return placementVisitRepository.findPlacementVisitById(placementVisitId);
    }

    public List<List<Placement>> getSmartVisitPlanSuggestions(String schoolName){

        List<Placement> placementList = placementService.getPlacementsBySchool(schoolName);

        List<List<Placement>> smartVisitPlanSuggestions = new ArrayList<>();
        boolean[] isAddedToSmartVisitList = new boolean[placementList.size()];
        Arrays.fill(isAddedToSmartVisitList, false);

        for(int i=0;i<placementList.size();i++){
            List<Placement> smartVisitPlanSuggestion = new ArrayList<>();
            boolean isThisPlacementPartOfSmartPlan = false;

            for(int j=i+1; j<placementList.size();j++){
                if(isAddedToSmartVisitList[j] == false &&
                        calculateDistance(placementList.get(i).getPlacementProvider().getLatitude(),
                            placementList.get(i).getPlacementProvider().getLongitude(),
                            placementList.get(j).getPlacementProvider().getLatitude(),
                            placementList.get(j).getPlacementProvider().getLongitude()) <= 15){
                    isThisPlacementPartOfSmartPlan = true;
                    isAddedToSmartVisitList[j] = true;
                    smartVisitPlanSuggestion.add(placementList.get(j));
                }
            }

            if(isThisPlacementPartOfSmartPlan){
                isAddedToSmartVisitList[i] = true;
                smartVisitPlanSuggestion.add(placementList.get(i));
            }

            if(!smartVisitPlanSuggestion.isEmpty()){
                smartVisitPlanSuggestions.add(smartVisitPlanSuggestion);
            }
        }

        if(smartVisitPlanSuggestions.isEmpty()){
            LOG.error("No smart visit plan suggestions");
            throw new ResourceNotFoundException("No smart visit plan suggestions");
        }

        return smartVisitPlanSuggestions;
    }

    private double calculateDistance(double startLat, double startLong, double endLat, double endLong) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (EARTH_RADIUS * c)/1.6;
    }

    private double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

}
