package sd.insoft.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import sd.insoft.model.Company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:httpQuery.properties")
public class CompanyRepositoryImpl implements CompanyRepository {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${yandex.maps.url}")
    private String url;
    @Value("${yandex.maps.api}")
    private String api;
    @Value("${yandex.maps.text}")
    private String text;
    @Value("${yandex.maps.lang}")
    private String lang;

    private String httpQuery;

    @Override
    public List<Company> getAllCompany(String query) {
        httpQuery = url + api + "&text=" + query + lang;
        String jsonString = null;

        boolean isFeatureCollection = false;
        JsonNode jsonNode = null;
        try {
            jsonString = getJsonString(httpQuery);
            jsonNode = new ObjectMapper().readTree(jsonString);
            isFeatureCollection = jsonNode.get("type").asText().equals("FeatureCollection");
            if (!isFeatureCollection) return null;
            jsonNode = jsonNode.get("features");
        } catch (IOException e) {
            log.error("Exception: {}", e);
        }

        List<Company> companyList = new ArrayList<>();

        for (JsonNode node : jsonNode) {
            Company company = new Company();
            JsonNode properties = node.get("properties");
            JsonNode tmp;
            if (properties != null) {
                if ((tmp = properties.get("description")) != null) company.setDescription(tmp.asText());
                JsonNode companyMetaData = properties.get("CompanyMetaData");
                if (companyMetaData != null) {
                    if ((tmp = companyMetaData.get("name")) != null) company.setName(tmp.asText());
                    if ((tmp = companyMetaData.get("address")) != null) company.setAddress(tmp.asText());
                    if ((tmp = companyMetaData.get("url")) != null) company.setUrl(tmp.asText());

                    StringBuilder phoneString = new StringBuilder();
                    JsonNode phones = companyMetaData.get("Phones");
                    if (phones != null) {
                        for (JsonNode phone : phones)
                            if ((tmp = phone.get("formatted")) != null) phoneString.append(tmp).append(" ");
                    }

                    company.setPhone(phoneString.toString());

                    if ((tmp = companyMetaData.get("Hours")) != null && (tmp = tmp.get("text")) != null)
                        company.setHoursText(tmp.asText());
                }

            }
            companyList.add(company);
        }

        return companyList;
    }
}
