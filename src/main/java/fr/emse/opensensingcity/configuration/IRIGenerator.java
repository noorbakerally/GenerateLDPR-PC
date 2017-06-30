package fr.emse.opensensingcity.configuration;

import java.net.URI;

/**
 * Created by noor on 28/06/17.
 */
public class IRIGenerator {
    public static String getSlug(RelatedResource rr, String slugTemplate){
        int i=0;
        while (i<slugTemplate.length()){
            if (slugTemplate.charAt(i)=='{'){
                int j = i;
                while (j<slugTemplate.length()){
                    if (slugTemplate.charAt(j)=='}'){
                        String varTemplate = slugTemplate.substring(i+1,j);
                        String varValue = processVarTemplate(varTemplate,rr);
                        slugTemplate = slugTemplate.replace("{"+varTemplate+"}",varValue);
                        break;
                    }
                    j++;
                }
            }
            i++;
        }
        return slugTemplate;
    }

    private static String processVarTemplate(String varTemplate, RelatedResource rr) {
        //rr.getFinalGraph().write(System.out,"TTL");

        URI uri = URI.create(rr.getIRI());
        if (varTemplate.contains("_rr.path[")){
            String pathStr = varTemplate.replace("_rr.path[","").replace("]","");
            int path = Integer.valueOf(pathStr);
            String[] pathParts = uri.getRawPath().replace("/", " ").trim().split(" ");
            return String.valueOf(pathParts[path]);
        }
        return varTemplate;

    }


}
