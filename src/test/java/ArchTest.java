import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchTest {
    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer(){
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example.oauth2");

        noClasses()
                .that()
                .resideInAnyPackage("..service..")
                .or()
                .resideInAnyPackage("..repositories..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..com.example.oauth2.web..")
                .because("Services and repositories should not depend on web layer")
                .check(importedClasses);
    }
}
