package org.slc.sli.util.transform;

public class StudentAssessment implements MongoDataEmitter 
{
	// Student ID,Performance Level,State,Family,Year,Subject,Grade Level,Assessment ID
	// 862,3,NC,EOG,2010-2011,Math,8,1
	private String studentId = null;
	private String performanceLevel = null;
	private String assessmentId = null;
	private String generatedUuid = null;

	public StudentAssessment(String studentId, String performanceLevel, String assessmentId)
	{
		this.studentId = studentId;
		this.performanceLevel = performanceLevel;
		this.assessmentId = assessmentId;
		generatedUuid = Base64.nextUuid("aabb");
	}

	public Object getStudentId() {
		return studentId;
	}

	@Override
	public String emit()
	{
		// {"_id":{"$binary":"fU9ha/veDR5aEXZWuzPDuA==","$type":"03"},"type":"studentAssessmentAssociation",
		// "body":{"studentId":"7afddec3-89ec-402c-8fe6-cced79ae3ef5","assessmentId":"6a53f63e-deb8-443d-8138-fc5a7368239c","administrationDate":"2011-09-15",
		// "administrationEndDate":"2011-12-15","retestIndicator":"1","scoreResults":[{"assessmentReportingMethod":"Raw_score","result":"85"}],"performanceLevel":"3"}}
		StringBuffer answer = new StringBuffer();
		
		String studentUuid = DataManager.getStudent(studentId).getUuid();
		String assessmentUuid = DataManager.getAssessment(assessmentId).getUuid();
		
		answer.append("{\"_id\":{\"$binary\":\"").append(Base64.toBase64(generatedUuid)).append("\",\"$type\":\"03\"},\"type\":\"studentAssessmentAssociation\",").
			append("\"body\":{\"studentId\":\"").append(studentUuid).append("\",\"assessmentId\":\"").append(assessmentUuid).append("\",\"administrationDate\":\"2011-09-15\",").
			append("\"administrationEndDate\":\"2011-12-15\",\"retestIndicator\":\"Primary Administration\",\"performanceLevel\":\"").append(performanceLevel).append("\"}}\n");
		
		return answer.toString();
	}
}
