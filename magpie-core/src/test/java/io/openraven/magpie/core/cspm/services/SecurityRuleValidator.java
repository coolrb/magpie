package io.openraven.magpie.core.cspm.services;

import com.fasterxml.jackson.core.type.TypeReference;
import io.openraven.magpie.core.cspm.analysis.IgnoredRule;
import io.openraven.magpie.core.cspm.analysis.Violation;
import io.openraven.magpie.core.cspm.model.Rule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * We avoid using IT prefix to skip failsafe plugin picked this test so far
 * while security-rules structure is not merged
 */
public class SecurityRuleValidator extends AbstractRuleValidator {

  private static final String TEST_RESOURCES_PROPERTY = "testResourcePath";
  private static final String TEST_RESOURCES_SUBPATH = "/resources/tests";

  @ParameterizedTest
  @MethodSource("getResourceFiles")
  public void testSecurityRules(File testRuleResourceFile) throws Exception {

    var ruleTestResource = MAPPER.readValue(testRuleResourceFile, new TypeReference<RuleTestResource>() {});
    Rule rule = ruleMap.get(ruleTestResource.getRuleId());

    final var filename = testRuleResourceFile.getName();
    final var ruleId =  ruleTestResource.getRuleId();
    assertTrue(filename.contains(ruleId), "ruleId is matching filename");

    // Insecure asset verification
    String insecureAssetGroup = ruleTestResource.getInsecureAssetGroup();
    List<Violation> violations = executeRule(insecureAssetGroup, rule);
    assertEquals(1, violations.size(), () -> reportAssertion(filename, ruleId, "violated assets size"));

    Violation violation = violations.get(0);
    assertEquals(ruleTestResource.getViolatedAssetId(), violation.getAssetId(),
      () -> reportAssertion(filename, ruleId, "violated asset"));
    assertNotEquals(ruleTestResource.getControlAssetId(), violation.getAssetId(),
      () -> reportAssertion(filename, ruleId, "secure asset"));

    cleanupAssets(); // Clean DB state before secure asset verification

    // Secure asset verification
    String secureAssetGroup = ruleTestResource.getSecureAssetGroup();
    List<Violation> secureSetupViolations = executeRule(secureAssetGroup, rule);
    assertEquals(0, secureSetupViolations.size(),
      () -> reportAssertion(filename, ruleId, "secure setup violations"));
  }

  private static Stream<Arguments> getResourceFiles() {
    String testResourceProp = System.getProperty(TEST_RESOURCES_PROPERTY);
    if (testResourceProp == null) {
      testResourceProp = getTargetProjectDirectoryPath() + TEST_RESOURCES_SUBPATH;
    }

    File testResourcePath = new File(testResourceProp);
    if (testResourcePath.isDirectory()) {
      return Arrays.stream(Objects.requireNonNull(testResourcePath.listFiles())).map(Arguments::of);
    }
    return Stream.of(Arguments.of(testResourcePath));
  }

  private List<Violation> executeRule(String assetGropupYml, Rule rule) throws Exception {
    populateAssetData(assetGropupYml);

    List<Violation> violations = new ArrayList<>();
    List<IgnoredRule> ignoredRules = new ArrayList<>();

    analyzeRule(violations, ignoredRules, rule);

    // All covered rules should be executed
    assertEquals(0, ignoredRules.size(), () -> "Provided rule ignored: " + rule.getRuleId());

    return violations;
  }

  private String reportAssertion(String filename, String ruleId, String assertion) {
    return String.format("Test resource: %s, Rule: %s - validation failed for: %s", filename, ruleId, assertion);
  }

}
