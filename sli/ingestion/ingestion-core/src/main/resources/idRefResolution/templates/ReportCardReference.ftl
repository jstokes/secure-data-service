<ReportCardIdentity>
    <StudentReference>
        <StudentIdentity>
            <StudentUniqueStateId>${ReportCard.StudentReference.StudentIdentity.StudentUniqueStateId}</StudentUniqueStateId>
        </StudentIdentity>
    </StudentReference>
    <GradingPeriodReference>
        <GradingPeriodIdentity>
            <EducationalOrgReference>
                <EducationalOrgIdentity>
                    <StateOrganizationId>${ReportCard.GradingPeriodReference.GradingPeriodIdentity.EducationalOrgReference.EducationalOrgIdentity.StateOrganizationId}</StateOrganizationId>
                </EducationalOrgIdentity>
            </EducationalOrgReference>
            <GradingPeriod>${ReportCard.GradingPeriodReference.GradingPeriodIdentity.GradingPeriod}</GradingPeriod>
            <BeginDate>${ReportCard.GradingPeriodReference.GradingPeriodIdentity.BeginDate}</BeginDate>
        </GradingPeriodIdentity>
    </GradingPeriodReference>
</ReportCardIdentity>