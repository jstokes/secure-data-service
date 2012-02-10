package org.talend.designer.codegen.translators.databases.netezza;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class TNetezzaRowBeginJava
{
  protected static String nl;
  public static synchronized TNetezzaRowBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TNetezzaRowBeginJava result = new TNetezzaRowBeginJava();
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
  protected final String TEXT_7 = NL + "    String dbProperties_";
  protected final String TEXT_8 = " = ";
  protected final String TEXT_9 = ";" + NL + "    String url_";
  protected final String TEXT_10 = " = null;" + NL + "    if(dbProperties_";
  protected final String TEXT_11 = " == null || dbProperties_";
  protected final String TEXT_12 = ".trim().length() == 0) {" + NL + "        url_";
  protected final String TEXT_13 = " = \"jdbc:netezza://\" + ";
  protected final String TEXT_14 = " + \":\" + ";
  protected final String TEXT_15 = " + \"/\" + ";
  protected final String TEXT_16 = ";" + NL + "    } else {" + NL + "        url_";
  protected final String TEXT_17 = " = \"jdbc:netezza://\" + ";
  protected final String TEXT_18 = " + \":\" + ";
  protected final String TEXT_19 = " + \"/\" + ";
  protected final String TEXT_20 = " + \"?\" + ";
  protected final String TEXT_21 = ";" + NL + "    }" + NL + "    String dbUser_";
  protected final String TEXT_22 = " = ";
  protected final String TEXT_23 = ";" + NL + "    String dbPwd_";
  protected final String TEXT_24 = " = ";
  protected final String TEXT_25 = ";    " + NL + "\tjava.lang.Class.forName(\"";
  protected final String TEXT_26 = "\");" + NL + "\tconn_";
  protected final String TEXT_27 = " = java.sql.DriverManager.getConnection(url_";
  protected final String TEXT_28 = ", dbUser_";
  protected final String TEXT_29 = ", dbPwd_";
  protected final String TEXT_30 = ");" + NL + "\t";
  protected final String TEXT_31 = NL;
  protected final String TEXT_32 = NL + "        if(conn_";
  protected final String TEXT_33 = ".getAutoCommit()) {" + NL + "            conn_";
  protected final String TEXT_34 = ".setAutoCommit(false);" + NL + "        }        " + NL + "        int commitEvery_";
  protected final String TEXT_35 = " = ";
  protected final String TEXT_36 = ";" + NL + "        int commitCounter_";
  protected final String TEXT_37 = " = 0;";
  protected final String TEXT_38 = NL;
  protected final String TEXT_39 = NL + "\tjava.sql.PreparedStatement pstmt_";
  protected final String TEXT_40 = " = conn_";
  protected final String TEXT_41 = ".prepareStatement(";
  protected final String TEXT_42 = ");\t";
  protected final String TEXT_43 = NL + "\tjava.sql.Statement stmt_";
  protected final String TEXT_44 = " = conn_";
  protected final String TEXT_45 = ".createStatement();";
  protected final String TEXT_46 = NL + "String query_";
  protected final String TEXT_47 = " = \"\";" + NL + "boolean whetherReject_";
  protected final String TEXT_48 = " = false;" + NL + "int nb_";
  protected final String TEXT_49 = " = 0;" + NL + "globalMap.put(\"";
  protected final String TEXT_50 = "_NB_EFFECTED\",nb_";
  protected final String TEXT_51 = ");";
  protected final String TEXT_52 = NL;

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
	String dbproperties = ElementParameterParser.getValue(node, "__PROPERTIES__");
	String dbuser= ElementParameterParser.getValue(node, "__USER__");
	String dbpwd= ElementParameterParser.getValue(node, "__PASS__");
    String commitEvery = ElementParameterParser.getValue(node, "__COMMIT_EVERY__");
    String dbquery= ElementParameterParser.getValue(node, "__QUERY__");
	       dbquery = dbquery.replaceAll("\n"," ");
    	   dbquery = dbquery.replaceAll("\r"," ");
    
	boolean usePrepareStatement = "true".equals(ElementParameterParser.getValue(node,"__USE_PREPAREDSTATEMENT__"));

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    
String useExistingConn = ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__");
if(("true").equals(useExistingConn)) {
	String connection = ElementParameterParser.getValue(node,"__CONNECTION__");
	String conn = "conn_" + connection;;
	
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(conn);
    stringBuffer.append(TEXT_6);
    
} else {
	String javaDbDriver   = "org.netezza.Driver";
	
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(dbproperties);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(dbhost);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(dbport);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(dbname);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(dbhost);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(dbport);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(dbname);
    stringBuffer.append(TEXT_20);
    stringBuffer.append(dbproperties);
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(dbuser);
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(dbpwd);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(javaDbDriver );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    
}	

    stringBuffer.append(TEXT_31);
    
if(!("true").equals(useExistingConn)) {
    if(!("").equals(commitEvery) && !("0").equals(commitEvery)) {
        
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(commitEvery);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    
    }
}

    stringBuffer.append(TEXT_38);
    
	if (usePrepareStatement ) {

    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(dbquery);
    stringBuffer.append(TEXT_42);
    
	} else {

    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    
	}

    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(TEXT_52);
    return stringBuffer.toString();
  }
}
