package sd.insoft.repo;

import sd.insoft.model.Company;

import java.util.List;

public interface CompanyRepository extends HttpObjectRepository {

    List<Company> getAllCompany(String query);

}
