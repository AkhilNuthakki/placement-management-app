package com.project.placement_management_app.repository;

import com.project.placement_management_app.model.PlacementAuthorizationRequest;
import com.project.placement_management_app.model.RequestCountGroupByStatus;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndCourse;
import com.project.placement_management_app.model.RequestCountGroupByStatusAndProviderName;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PlacementAuthorizationRequestRepository extends MongoRepository<PlacementAuthorizationRequest, String> {

    @Query(value = "{_id:'?0'}")
    PlacementAuthorizationRequest findPlacementAuthRequestById(String Id);

    @Query(value = "{userId:'?0'}")
    List<PlacementAuthorizationRequest> findPlacementAuthRequestsByUserId(String userId);

    @Query(value = "{$and:[{'student.school':'?0'},{requestSubmissionStatus:'?1'}]}")
    List<PlacementAuthorizationRequest> findPlacementAuthRequestsBySchoolAndStatus(String school, String requestSubmissionStatus);

    @Query(value = "{$and:[{'placementProvider.contactEmail':'?0'},{requestSubmissionStatus:'?1'}]}")
    List<PlacementAuthorizationRequest> findPlacementAuthRequestsByProviderContactEmailIdAndStatus(String providerContactEmailId, String requestSubmissionStatus);

    @Aggregation(pipeline = {
            "{'$group': {_id: '$placementProvider.name'}}" ,
                    "{'$project' : {_id:0,name:'$_id'}}"
    })
    List<String> findDistinctPlacementProviderName();

    @Aggregation(pipeline = {
            "{'$match' : {'placementProvider.name' : '?0'}}" ,
            "{'$group' : {_id : '$placementAuthorizationRequestStatus',count : {'$sum' : 1}}}" ,
            "{'$project' : {_id :0,status:'$_id',count : 1}}"
    })
    List<RequestCountGroupByStatus> findRequestsFromProviderGroupByStatus(String providerName);

    @Aggregation(pipeline = {
            "{'$match' : {'student.school' : '?0'}}" ,
            "{'$group' : {_id : '$placementAuthorizationRequestStatus',count : {'$sum' : 1}}}" ,
            "{'$project' : {_id :0,status:'$_id',count : 1}}"
    })
    List<RequestCountGroupByStatus> findRequestsFromSchoolGroupByStatus(String schoolName);

    @Aggregation(pipeline = {
            "{'$match' : {'student.school' : '?0'}}" ,
            "{'$group' : {_id : {course: '$student.course',status: '$placementAuthorizationRequestStatus'},count : {'$sum' : 1}}}" ,
            "{'$group' : {_id: '$_id.course', statuses: {$push : {status : '$_id.status', count : '$count'}}}}",
            "{'$project' : {course: '$_id', _id :0, statuses: 1}}"
    })
    List<RequestCountGroupByStatusAndCourse> findRequestsFromSchoolGroupByStatusAndCourse(String schoolName);

    @Aggregation(pipeline = {
            "{'$match' : {'student.school' : '?0'}}" ,
            "{'$group' : {_id : {providerName: '$placementProvider.name',status: '$placementAuthorizationRequestStatus'},count : {'$sum' : 1}}}" ,
            "{'$group' : {_id: '$_id.providerName', statuses: {'$push' : {status : '$_id.status', count : '$count'}}}}",
            "{'$project': {providerName: '$_id', _id: 0, statuses: 1}}"
    })
    List<RequestCountGroupByStatusAndProviderName> findRequestsFromSchoolGroupByStatusAndProviderName(String schoolName);



}
