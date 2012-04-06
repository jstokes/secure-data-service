<div class="tabular">
    <table ><thead><tr><th></th><td class="singleOfSixColumns"><h6>${singleContactName}</h6></td></tr></thead><tbody>
    <tr><th></th><td></td></tr>
	<!-- display telephone numbers for student -->
		<#list singleContact.telephone as telephone>
		<tr>
				<th>
					${telephone.telephoneNumberType}:
				</th>
				<td class="singleOfSixColumns">
					${telephone.telephoneNumber}
				</td>
			</tr>
		</#list>
	<tr><th></th><td></td></tr>
		<#list singleContact.electronicMail as electronicMail>
			<tr>
				<th>
					<!-- show only once -->
					<#if electronicMail_index == 0>
					E-mail:
					</#if>
				</th>
				<td class="singleOfSixColumns">
					${electronicMail.emailAddress}
				</td>
			</tr>
		</#list>
	<tr><th></th><td></td></tr>
		<#list singleContact.address as address>
			<tr>
				<th>
				<!-- show only once -->
				<#if address_index == 0>
					Address:
				</#if>
				</th>
				<td class="singleOfSixColumns">
				    <div>
					${address.streetNumberName}<#if address.apartmentRoomSuiteNumber ??>, ${address.apartmentRoomSuiteNumber}</#if>
					<!-- 
					ignore apartmentRoomSuiteNumber is null.
					otherwise display on the first line separated by comma after streetNumberName
					-->
					
					
					<!--
					ignore BuildingSiteNumber if null otherwise display it on its own line
					-->
					</div>
					<#if address.buildingSiteNumber ??>
					
					<div>
					${address.buildingSiteNumber}
					</div>
					</#if>
					
					<div>
					${address.city}, ${address.stateAbbreviation} ${address.postalCode}
					</div>
					<#if address.countryCode ?? && address.countryCode != "US">
						<div>
						${address.countryCode}
						</div>
					</#if>
				</td>
			</tr>
		</#list>
	</tbody></table>
</div>
