package datasource;

/**
 * User: Cynric
 * Date: 14-4-26
 * Time: 15:52
 */
public enum TestFilterString {
    EQ("Name eq 'Cynric'"),
    GE("Age ge 22"),
    AND("Age ge 22 and BirthDate eq datetime'1992-03-22T11:46:40.000'"),
    NOT("not (Age ge 22) or Name eq 'Cynric'"),
    NOT2("not (Age ge 50 or Name ne 'Cynric')"),
    SUBSTRING3("substring(Name, 1, 2) eq 'yn'");

    String filterString;

    TestFilterString(String filter) {
        this.filterString = filter;
    }

    public String toString() {
        return this.filterString;
    }
}
