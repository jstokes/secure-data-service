package org.talend.designer.codegen.translators.business.salesforce;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.process.IConnectionCategory;
import java.util.List;

public class TSalesforceGetUpdatedBeginJava
{
  protected static String nl;
  public static synchronized TSalesforceGetUpdatedBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TSalesforceGetUpdatedBeginJava result = new TSalesforceGetUpdatedBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + NL + "int nb_line_";
  protected final String TEXT_3 = " = 0;" + NL;
  protected final String TEXT_4 = NL + "    java.util.Properties props_";
  protected final String TEXT_5 = " = System.getProperties();" + NL + "    props_";
  protected final String TEXT_6 = ".put(\"socksProxyHost\",";
  protected final String TEXT_7 = ");  " + NL + "    props_";
  protected final String TEXT_8 = ".put(\"socksProxyPort\",";
  protected final String TEXT_9 = ");" + NL + "    props_";
  protected final String TEXT_10 = ".put(\"java.net.socks.username\", ";
  protected final String TEXT_11 = ");" + NL + "    props_";
  protected final String TEXT_12 = ".put(\"java.net.socks.password\", ";
  protected final String TEXT_13 = "); " + NL + "    ";
  protected final String TEXT_14 = NL;
  protected final String TEXT_15 = NL + "\t\t\tjava.text.SimpleDateFormat startdf_";
  protected final String TEXT_16 = " = new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");" + NL + "\t\t    java.text.SimpleDateFormat enddf_";
  protected final String TEXT_17 = " = new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");" + NL + "\t\t\t" + NL + "\t\t\tstartdf_";
  protected final String TEXT_18 = ".parse(";
  protected final String TEXT_19 = ");" + NL + "\t\t    enddf_";
  protected final String TEXT_20 = ".parse(";
  protected final String TEXT_21 = ");" + NL + "\t\t    " + NL + "\t\t    java.util.Calendar startCal_";
  protected final String TEXT_22 = " = startdf_";
  protected final String TEXT_23 = ".getCalendar();" + NL + "\t\t    java.util.Calendar endCal_";
  protected final String TEXT_24 = " = enddf_";
  protected final String TEXT_25 = ".getCalendar();" + NL + "\t\t\t" + NL + "\t\t\torg.talend.salesforce.SforceManagement sfMgr_";
  protected final String TEXT_26 = " = new org.talend.salesforce.SforceManagementImpl();" + NL + "\t\t\t";
  protected final String TEXT_27 = NL + "\t\t\t\tif(globalMap.get(\"stub_";
  protected final String TEXT_28 = "\")==null || globalMap.get(\"sessionHeader_";
  protected final String TEXT_29 = "\")==null){" + NL + "\t\t\t\t\tthrow new RuntimeException(\"Get null connection from ";
  protected final String TEXT_30 = "\");" + NL + "\t\t\t\t}" + NL + "\t\t\t\tif(globalMap.get(\"callOptions_";
  protected final String TEXT_31 = "\")!=null ){" + NL + "\t\t\t\t\tsfMgr_";
  protected final String TEXT_32 = ".setCallOptions((com.salesforce.soap.partner.CallOptions)globalMap.get(\"callOptions_";
  protected final String TEXT_33 = "\"));" + NL + "\t\t\t\t}" + NL + "\t\t\t\tsfMgr_";
  protected final String TEXT_34 = ".login((com.salesforce.soap.partner.SforceServiceStub)globalMap.get(\"stub_";
  protected final String TEXT_35 = "\"),(com.salesforce.soap.partner.SessionHeader)globalMap.get(\"sessionHeader_";
  protected final String TEXT_36 = "\"));" + NL + "\t\t\t";
  protected final String TEXT_37 = NL + "\t\t\t\t";
  protected final String TEXT_38 = NL + "\t\t\t\t\tsfMgr_";
  protected final String TEXT_39 = ".setClientID(";
  protected final String TEXT_40 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_41 = NL + "\t\t\t\tboolean result_";
  protected final String TEXT_42 = " = sfMgr_";
  protected final String TEXT_43 = ".login(";
  protected final String TEXT_44 = ",";
  protected final String TEXT_45 = ", ";
  protected final String TEXT_46 = ",";
  protected final String TEXT_47 = ",";
  protected final String TEXT_48 = ");" + NL + "\t\t\t\tif(!result_";
  protected final String TEXT_49 = "){" + NL + "\t\t\t\t\tthrow new RuntimeException(\"Login failed! Please check the username,password and endpoint\");" + NL + "\t\t\t\t}" + NL + "\t\t\t";
  protected final String TEXT_50 = "\t\t" + NL + "\t" + NL + "\t\t\t";
  protected final String TEXT_51 = "\t\t\t\t" + NL + "" + NL + "\tcom.salesforce.soap.partner.sobject.SObject[] so_";
  protected final String TEXT_52 = " = null;" + NL + "\tcom.salesforce.soap.partner.ID[] ids_";
  protected final String TEXT_53 = " = sfMgr_";
  protected final String TEXT_54 = ".getUpdated(\"";
  protected final String TEXT_55 = "\", startCal_";
  protected final String TEXT_56 = ", endCal_";
  protected final String TEXT_57 = ");" + NL + "\t" + NL + "\tif(ids_";
  protected final String TEXT_58 = " !=null && ids_";
  protected final String TEXT_59 = ".length > 0){" + NL + "\t\t\t\t\t\t" + NL + "\t\tint newMax_";
  protected final String TEXT_60 = " = 0;" + NL + "\t\tint newBase_";
  protected final String TEXT_61 = " = 0;" + NL + "\t\t" + NL + "\t\twhile (newMax_";
  protected final String TEXT_62 = " < ids_";
  protected final String TEXT_63 = ".length){" + NL + "\t\t" + NL + "\t\t\tnewBase_";
  protected final String TEXT_64 = " = newMax_";
  protected final String TEXT_65 = ";" + NL + "\t\t\tnewMax_";
  protected final String TEXT_66 = " += 2000;" + NL + "\t\t\t" + NL + "\t\t\tif (newMax_";
  protected final String TEXT_67 = " > ids_";
  protected final String TEXT_68 = ".length){" + NL + "\t\t\t\tnewMax_";
  protected final String TEXT_69 = " = ids_";
  protected final String TEXT_70 = ".length;" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\tcom.salesforce.soap.partner.ID[] localIds_";
  protected final String TEXT_71 = " = new com.salesforce.soap.partner.ID[newMax_";
  protected final String TEXT_72 = "-newBase_";
  protected final String TEXT_73 = "];" + NL + "" + NL + "\t\t\tfor (int i_";
  protected final String TEXT_74 = "=newBase_";
  protected final String TEXT_75 = ";i_";
  protected final String TEXT_76 = " < newMax_";
  protected final String TEXT_77 = ";i_";
  protected final String TEXT_78 = "++){" + NL + "\t\t\t\tlocalIds_";
  protected final String TEXT_79 = "[i_";
  protected final String TEXT_80 = "-newBase_";
  protected final String TEXT_81 = "] = ids_";
  protected final String TEXT_82 = "[i_";
  protected final String TEXT_83 = "];" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "\t\t\tso_";
  protected final String TEXT_84 = " = sfMgr_";
  protected final String TEXT_85 = ".retrieve(localIds_";
  protected final String TEXT_86 = ",\"";
  protected final String TEXT_87 = "\",\"";
  protected final String TEXT_88 = "\");" + NL + "    \t\t" + NL + "    \t\tif(so_";
  protected final String TEXT_89 = " != null){" + NL + "    \t\t" + NL + "\t\t\tfor (int i_";
  protected final String TEXT_90 = "=0;i_";
  protected final String TEXT_91 = "<so_";
  protected final String TEXT_92 = ".length;i_";
  protected final String TEXT_93 = "++){" + NL + "\t\t\t\tnb_line_";
  protected final String TEXT_94 = " ++;" + NL + "\t\t\t\t" + NL + "\t\t\t  if(so_";
  protected final String TEXT_95 = "[i_";
  protected final String TEXT_96 = "]!=null){" + NL + "\t\t\t\torg.apache.axiom.om.OMElement[] fields_";
  protected final String TEXT_97 = " = so_";
  protected final String TEXT_98 = "[i_";
  protected final String TEXT_99 = "].getExtraElement();" + NL + "\t\t\t\tint len_";
  protected final String TEXT_100 = " = fields_";
  protected final String TEXT_101 = ".length;";
  protected final String TEXT_102 = "\t\t\t\t\t\t" + NL + "\t\t\t\t    if (";
  protected final String TEXT_103 = " < len_";
  protected final String TEXT_104 = " &&  fields_";
  protected final String TEXT_105 = "[";
  protected final String TEXT_106 = "]!=null && fields_";
  protected final String TEXT_107 = "[";
  protected final String TEXT_108 = "].getText() != null && !\"\".equals(fields_";
  protected final String TEXT_109 = "[";
  protected final String TEXT_110 = "].getText())){";
  protected final String TEXT_111 = NL + "\t\t\t\t\t";
  protected final String TEXT_112 = ".";
  protected final String TEXT_113 = " = fields_";
  protected final String TEXT_114 = "[";
  protected final String TEXT_115 = "].getText();";
  protected final String TEXT_116 = NL + "\t\t\t\t\t";
  protected final String TEXT_117 = ".";
  protected final String TEXT_118 = " = ParserUtils.parseTo_Date(fields_";
  protected final String TEXT_119 = "[";
  protected final String TEXT_120 = "].getText(), ";
  protected final String TEXT_121 = ");";
  protected final String TEXT_122 = NL + "\t\t\t\t\t";
  protected final String TEXT_123 = ".";
  protected final String TEXT_124 = " = fields_";
  protected final String TEXT_125 = "[";
  protected final String TEXT_126 = "].getText().getBytes();";
  protected final String TEXT_127 = "\t\t\t\t\t\t" + NL + "\t\t\t\t\t";
  protected final String TEXT_128 = ".";
  protected final String TEXT_129 = " = ParserUtils.parseTo_";
  protected final String TEXT_130 = "(fields_";
  protected final String TEXT_131 = "[";
  protected final String TEXT_132 = "].getText());";
  protected final String TEXT_133 = "\t\t\t" + NL + "\t\t\t\t\t} else {" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_134 = ".";
  protected final String TEXT_135 = " = ";
  protected final String TEXT_136 = ";" + NL + "\t\t\t\t\t}";
  protected final String TEXT_137 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();

	boolean useProxy = ("true").equals(ElementParameterParser.getValue(node,"__USE_PROXY__"));
	String proxyHost = ElementParameterParser.getValue(node,"__PROXY_HOST__");
   	String proxyPort = ElementParameterParser.getValue(node,"__PROXY_PORT__");
   	String proxyUsername = ElementParameterParser.getValue(node,"__PROXY_USERNAME__");
   	String proxyPassword = ElementParameterParser.getValue(node,"__PROXY_PASSWORD__");
   	boolean needCompression = ("true").equals(ElementParameterParser.getValue(node,"__NEED_COMPRESSION__"));

String cid = node.getUniqueName();

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    
if(useProxy){

    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(proxyHost );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(proxyPort );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(proxyUsername );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(proxyPassword );
    stringBuffer.append(TEXT_13);
    
}

    stringBuffer.append(TEXT_14);
    
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas != null) && (metadatas.size() > 0)) {
	IMetadataTable metadata = metadatas.get(0);
	
	if (metadata != null) {
	
		List<IMetadataColumn> columnList = metadata.getListColumns(); 
		List<? extends IConnection> outgoingConns = node.getOutgoingSortedConnections();

		if (outgoingConns != null && outgoingConns.size() > 0){
			
			String sd = ElementParameterParser.getValue(node, "__START_DATE__");
			String ed = ElementParameterParser.getValue(node, "__END_DATE__");

			String username = ElementParameterParser.getValue(node, "__USER__");
			String password = ElementParameterParser.getValue(node, "__PASS__");
			String timeout = ElementParameterParser.getValue(node,"__TIMEOUT__");
			String module = ElementParameterParser.getValue(node, "__MODULENAME__");
			String customModulename = ElementParameterParser.getValue(node, "__CUSTOM_MODULE_NAME__").trim();
			module = (("CustomModule").equals(module)) ? customModulename.replaceAll("\"","") : module;
			String endpoint = ElementParameterParser.getValue(node, "__ENDPOINT__");
			boolean useExistingConn = ("true").equals(ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__"));
			String connection = ElementParameterParser.getValue(node,"__CONNECTION__");
			String clientID = ElementParameterParser.getValue(node,"__CLIENT_ID__");
			
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(sd);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(ed);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    if(useExistingConn){
    stringBuffer.append(TEXT_27);
    stringBuffer.append(connection );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(connection);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(connection );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(connection );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(connection );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(connection );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(connection);
    stringBuffer.append(TEXT_36);
    }else{
    stringBuffer.append(TEXT_37);
    if(clientID!=null && !"".equals(clientID) && !"\"\"".equals(clientID)){
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(clientID);
    stringBuffer.append(TEXT_40);
    }
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(endpoint);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(username);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(password);
    stringBuffer.append(TEXT_46);
    stringBuffer.append(timeout);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(needCompression);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    }
    stringBuffer.append(TEXT_50);
    
			java.lang.StringBuilder sb = new java.lang.StringBuilder();
			for (IMetadataColumn column: columnList){
				if(sb.length() == 0){
					sb.append(column.getLabel());
				}else{
					sb.append(","+column.getLabel());
				}
			}
			
			String fieldlist = sb.toString();
			
			IConnection outgoingConn = outgoingConns.get(0);
			
			if(outgoingConn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) { // start 1

    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(module);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(module );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(fieldlist );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
     
						for( int i = 0; i < columnList.size(); i++) {
						
							IMetadataColumn column = columnList.get(i);
							
							String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
							
							JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
							
							String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();

    stringBuffer.append(TEXT_102);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_109);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_110);
    
									if (javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) { // String or Object

    stringBuffer.append(TEXT_111);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_112);
    stringBuffer.append(columnList.get(i).getLabel());
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_115);
    
									} else if(javaType == JavaTypesManager.DATE) { // Date

    stringBuffer.append(TEXT_116);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_117);
    stringBuffer.append(columnList.get(i).getLabel());
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_120);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_121);
    
									} else if(javaType == JavaTypesManager.BYTE_ARRAY) { // byte[]

    stringBuffer.append(TEXT_122);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_123);
    stringBuffer.append(columnList.get(i).getLabel());
    stringBuffer.append(TEXT_124);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_125);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_126);
    
									} else  { // other

    stringBuffer.append(TEXT_127);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_128);
    stringBuffer.append(columnList.get(i).getLabel());
    stringBuffer.append(TEXT_129);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_132);
     
									}

    stringBuffer.append(TEXT_133);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_134);
    stringBuffer.append(columnList.get(i).getLabel());
    stringBuffer.append(TEXT_135);
    stringBuffer.append(JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate));
    stringBuffer.append(TEXT_136);
     
						}
			}
		
		}
	}
}

    stringBuffer.append(TEXT_137);
    return stringBuffer.toString();
  }
}
