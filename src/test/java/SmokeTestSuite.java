import org.junit.platform.suite.api.*;

@SelectPackages("example")
@IncludeTags("smoke")
//@IncludeClassNamePatterns("^example.Test1$")
@Suite
public class SmokeTestSuite {
}
