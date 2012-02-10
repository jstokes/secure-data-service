package org.talend.designer.codegen.translators.databases.oracle;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class TOracleRowBeginJava
{
  protected static String nl;
  public static synchronized TOracleRowBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TOracleRowBeginJava result = new TOracleRowBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + NL + "java.sql.Connection conn_";
  protected final String TEXT_3 = " = null;";
  protected final String TEXT_4 = NL + "\tconn_";
  protected final String TEXT_5 = " = (java.sql.Connection)globalMap.get(\"";
  protected final String TEXT_6 = "\");" + NL + "\t";
  protected final String TEXT_7 = NL + "\t";
  protected final String TEXT_8 = NL + "    \tjava.lang.Class.forName(\"oracle.jdbc.OracleDriver\");\t";
  protected final String TEXT_9 = NL + "\t\tjava.lang.Class.forName(\"oracle.jdbc.driver.OracleDriver\");\t" + NL + "\t";
  protected final String TEXT_10 = NL + "\t";
  protected final String TEXT_11 = NL + "\t\tString url_";
  protected final String TEXT_12 = " = ";
  protected final String TEXT_13 = ";        " + NL + "\t\t";
  protected final String TEXT_14 = NL + "\t    String url_";
  protected final String TEXT_15 = " = \"jdbc:oracle:thin:@\" + ";
  protected final String TEXT_16 = " + \":\" + ";
  protected final String TEXT_17 = " + \":\" + ";
  protected final String TEXT_18 = ";" + NL + "\t    ";
  protected final String TEXT_19 = NL + "\t    String url_";
  protected final String TEXT_20 = " = \"jdbc:oracle:thin:@(description=(address=(protocol=tcp)(host=\" + ";
  protected final String TEXT_21 = " + \")(port=\" + ";
  protected final String TEXT_22 = " + \"))(connect_data=(service_name=\" + ";
  protected final String TEXT_23 = " + \")))\";" + NL + "\t    ";
  protected final String TEXT_24 = NL + "\t    String url_";
  protected final String TEXT_25 = " = \"jdbc:oracle:oci8:@\" + ";
  protected final String TEXT_26 = ";" + NL + "\t    ";
  protected final String TEXT_27 = NL + NL + "\tString dbUser_";
  protected final String TEXT_28 = " = ";
  protected final String TEXT_29 = ";" + NL + "\tString dbPwd_";
  protected final String TEXT_30 = " = ";
  protected final String TEXT_31 = ";";
  protected final String TEXT_32 = NL + "\tconn_";
  protected final String TEXT_33 = " = java.sql.DriverManager.getConnection(url_";
  protected final String TEXT_34 = ",dbUser_";
  protected final String TEXT_35 = ",dbPwd_";
  protected final String TEXT_36 = ");";
  protected final String TEXT_37 = NL + "\tString atnParams_";
  protected final String TEXT_38 = " = ";
  protected final String TEXT_39 = ";" + NL + "\tatnParams_";
  protected final String TEXT_40 = " = atnParams_";
  protected final String TEXT_41 = ".replaceAll(\"&\", \"\\n\");" + NL + "\tjava.util.Properties atnParamsPrope_";
  protected final String TEXT_42 = " = new java.util.Properties();" + NL + "\tatnParamsPrope_";
  protected final String TEXT_43 = ".put(\"user\",dbUser_";
  protected final String TEXT_44 = ");" + NL + "\tatnParamsPrope_";
  protected final String TEXT_45 = ".put(\"password\",dbPwd_";
  protected final String TEXT_46 = ");" + NL + "\tatnParamsPrope_";
  protected final String TEXT_47 = ".load(new java.io.ByteArrayInputStream(atnParams_";
  protected final String TEXT_48 = ".getBytes()));" + NL + "\tconn_";
  protected final String TEXT_49 = " = java.sql.DriverManager.getConnection(url_";
  protected final String TEXT_50 = ", atnParamsPrope_";
  protected final String TEXT_51 = ");";
  protected final String TEXT_52 = NL + "\t";
  protected final String TEXT_53 = NL;
  protected final String TEXT_54 = NL + "        if(conn_";
  protected final String TEXT_55 = ".getAutoCommit()) {" + NL + "            conn_";
  protected final String TEXT_56 = ".setAutoCommit(false);" + NL + "        }        " + NL + "        int commitEvery_";
  protected final String TEXT_57 = " = ";
  protected final String TEXT_58 = ";    " + NL + "        int commitCounter_";
  protected final String TEXT_59 = " = 0;    ";
  protected final String TEXT_60 = NL;
  protected final String TEXT_61 = NL + "\tjava.sql.PreparedStatement pstmt_";
  protected final String TEXT_62 = " = conn_";
  protected final String TEXT_63 = ".prepareStatement(";
  protected final String TEXT_64 = ");\t";
  protected final String TEXT_65 = NL + "\tjava.sql.Statement stmt_";
  protected final String TEXT_66 = " = conn_";
  protected final String TEXT_67 = ".createStatement();";
  protected final String TEXT_68 = NL + "String query_";
  protected final String TEXT_69 = " = \"\";" + NL + "boolean whetherReject_";
  protected final String TEXT_70 = " = false;" + NL;
  protected final String TEXT_71 = NL + "\t\tint nb_line_inserted_";
  protected final String TEXT_72 = " = 0;";
  protected final String TEXT_73 = NL + "\t\tint nb_line_update_";
  protected final String TEXT_74 = " = 0;";
  protected final String TEXT_75 = NL + "\t\tint nb_line_deleted_";
  protected final String TEXT_76 = " = 0;";
  protected final String TEXT_77 = NL + NL;
  protected final String TEXT_78 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	
	String dbhost = ElementParameterParser.getValue(node, "__HOST__");
	String dbport = ElementParameterParser.getValue(node, "__PORT__");
	String dbname= ElementParameterParser.getValue(node, "__DBNAME__");
	String localServiceName = ElementParameterParser.getValue(node, "__LOCAL_SERVICE_NAME__");
	String dbuser= ElementParameterParser.getValue(node, "__USER__");
	String dbpwd= ElementParameterParser.getValue(node, "__PASS__");
    String commitEvery = ElementParameterParser.getValue(node, "__COMMIT_EVERY__");
    String dbquery= ElementParameterParser.getValue(node, "__QUERY__");
	       dbquery = dbquery.replaceAll("\n"," ");
    	   dbquery = dbquery.replaceAll("\r"," ");
    
	boolean usePrepareStatement = "true".equals(ElementParameterParser.getValue(node,"__USE_PREPAREDSTATEMENT__"));
	String dbproperties = ElementParameterParser.getValue(node, "__PROPERTIES__");
	
	String use_NB_Line = ElementParameterParser.getValue(node, "__USE_NB_LINE__");
	String dbVersion =  ElementParameterParser.getValue(node, "__DB_VERSION__");

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    
String useExistingConn = ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__");
if(("true").equals(useExistingConn)) {
	String connection = ElementParameterParser.getValue(node,"__CONNECTION__");
	String conn = "conn_" + connection;
	
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(conn);
    stringBuffer.append(TEXT_6);
    
} else {   
	
    stringBuffer.append(TEXT_7);
    if("ojdbc5-11g.jar".equals(dbVersion) || "ojdbc6-11g.jar".equals(dbVersion) ){
    stringBuffer.append(TEXT_8);
    }else {
    stringBuffer.append(TEXT_9);
    }
    stringBuffer.append(TEXT_10);
    
	String connectionType = ElementParameterParser.getValue(node, "__CONNECTION_TYPE__");
	String rac_url = ElementParameterParser.getValue(node, "__RAC_URL__");
	if("ORACLE_RAC".equals(connectionType)) {
	
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(rac_url);
    stringBuffer.append(TEXT_13);
    
	} else if(("ORACLE_SID").equals(connectionType)) {
	    
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(dbhost);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(dbport);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(dbname);
    stringBuffer.append(TEXT_18);
    
	} else if(("ORACLE_SERVICE_NAME").equals(connectionType)) {
	    
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(dbhost);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(dbport);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(dbname);
    stringBuffer.append(TEXT_23);
    
	} else if(("ORACLE_OCI").equals(connectionType)) {
	    
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(localServiceName);
    stringBuffer.append(TEXT_26);
    
	}
	
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(dbuser);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(dbpwd);
    stringBuffer.append(TEXT_31);
    
	if(dbproperties == null || ("\"\"").equals(dbproperties) || ("").equals(dbproperties)) {

    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_36);
    
	} else {

    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(dbproperties);
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_51);
    
	}

    stringBuffer.append(TEXT_52);
    
}

    stringBuffer.append(TEXT_53);
    
if(!("true").equals(useExistingConn)) {
    if(!("").equals(commitEvery) && !("0").equals(commitEvery)) {
        
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_57);
    stringBuffer.append(commitEvery);
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    
    }
}

    stringBuffer.append(TEXT_60);
    
	if (usePrepareStatement ) {

    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(dbquery);
    stringBuffer.append(TEXT_64);
    
	} else {

    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    
	}

    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    	//feature 0010425
	if ("NB_LINE_INSERTED".equals(use_NB_Line)) {

    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_72);
    
	} else if ("NB_LINE_UPDATED".equals(use_NB_Line)) {

    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_74);
    
	} else if ("NB_LINE_DELETED".equals(use_NB_Line)) {

    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_76);
    
	}//end feature 0010425

    stringBuffer.append(TEXT_77);
    stringBuffer.append(TEXT_78);
    return stringBuffer.toString();
  }
}
