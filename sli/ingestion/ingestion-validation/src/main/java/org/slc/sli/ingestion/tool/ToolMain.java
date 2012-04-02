package org.slc.sli.ingestion.tool;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ToolMain{

    public static void main(String [] args) throws IOException{
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring/validatorContext.xml");

    	ToolMain main = context.getBean(ToolMain.class);
    main.start(args);

    }

    private ValidationController controller;
    //Name of the validation tool
    final String appName;

    private void start(Map<String,String> map_args){

        if( (args.length != n_args + 1) ){
            System.out.println(appName + ":Illegal options");
            System.out.println("Usage: " + appName + "[directory]");
            return ;
        }

        String landing_zone = args[1];

        controller.doValidation(landing_zone);
    }

    public void setValidationController(ValidationController controller){
        this.controller = controller;
    }

    public ValidationController getValidation(){
        return controller;
    }
}


