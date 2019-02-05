import com.mbi.Faker;
import com.mbi.JsonFaker;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class JsonFakerTest {

    private final Faker jsonFaker = new JsonFaker();
    private final JSONObject json = jsonFaker.fakeData(new JSONObject().put("f", "{$caller}"));

    @Test
    public void testCanFakeData() {
        String date = DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime());
        JSONObject json = new JSONObject();
        json.put("a", "aaa {$caller} ccc {$uid} eee");
        json.put("b", new JSONObject().put("c", 2));
        json.put("d", "{$current_date} {$current_date}");
        json.put("e", new JSONObject().put("f", "{$current_date} {$current_date}"));
        json.put("k", new JSONArray().put(new JSONObject().put("f", "{$current_date} {$CURRENT_DATE}")));

        JSONObject result = jsonFaker.fakeData(json);

        assertFalse(result.toString().contains("{$"));
        assertTrue(result.getString("a").startsWith("aaa JsonFakerTest.testCanFakeData ccc "));
        result.getJSONObject("b").similar(new JSONObject().put("c", 2));
        assertEquals(result.getString("d"), date + " " + date);
        result.getJSONObject("e").similar(new JSONObject().put("f", date + " " + date));
        result.getJSONArray("k").getJSONObject(0).similar(new JSONObject().put("f", date + " " + date));
    }

    @Test
    public void testFakeWithIncorrectJson() {
        boolean passed;
        try {
            jsonFaker.fakeData("123");
            passed = true;
        } catch (JSONException ex) {
            passed = false;
            assertEquals(ex.getMessage(), "Incorrect json: 123");
        }
        assertFalse(passed);
    }

    @Test
    public void testFakeWithUpperCaseParameter() {
        String date = DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime());
        JSONObject json = new JSONObject();
        json.put("a", "aaa {$CURRENT_DATE} ccc");

        JSONObject result = jsonFaker.fakeData(json);

        String dt = result.getString("a");
        assertEquals(dt, "aaa " + date + " ccc");
    }

    @Test
    public void testFakeWithLowerCaseParameter() {
        String date = DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime());
        JSONObject json = new JSONObject();
        json.put("a", "aaa {$current_date} ccc");

        JSONObject result = jsonFaker.fakeData(json);

        String dt = result.getString("a");
        assertEquals(dt, "aaa " + date + " ccc");
    }

    @Test
    public void testFakeWithCamelCaseParameter() {
        String date = DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime());
        JSONObject json = new JSONObject();
        json.put("a", "aaa {$Current_Date} ccc");

        JSONObject result = jsonFaker.fakeData(json);

        String dt = result.getString("a");
        assertEquals(dt, "aaa " + date + " ccc");
    }

    @Test
    public void testSourceObjectNotUpdated() {
        JSONObject json1 = new JSONObject();
        json1.put("a", "aaa {$current_date} ccc {$current_date} eee");
        JSONObject json2 = new JSONObject(json1.toString());

        jsonFaker.fakeData(json1);

        json1.similar(json2);
    }

    @Test
    public void testSourceArrayNotUpdated() {
        JSONArray json1 = new JSONArray();
        json1.put(new JSONObject().put("a", "aaa {$current_date} ccc {$current_date} eee"));
        JSONArray json2 = new JSONArray(json1.toString());

        jsonFaker.fakeData(json1);

        json1.similar(json2);
    }

    @Test
    public void testInnerJsonObject() {
        JSONObject json = new JSONObject();
        json.put("e", new JSONObject().put("f", "{$current_date} {$current_date}"));
        json.put("g", new JSONObject().put("j", new JSONObject().put("f", "{$current_date} {$current_date}")));
        JSONObject j = new JSONObject();
        j.put("dd", json);
        JSONObject jj = new JSONObject();
        jj.put("ddd", j);

        JSONObject result = jsonFaker.fakeData(jj);

        assertFalse(result.toString().contains("{$"));
    }

    @Test
    public void testInnerJsonArray() {
        JSONObject json = new JSONObject();
        json.put("k", new JSONArray().put(new JSONObject().put("f", "{$current_date} {$current_date}")));

        JSONObject result = jsonFaker.fakeData(json);

        assertFalse(result.toString().contains("{$"));
    }

    @Test
    public void testJsonArray() {
        JSONObject json1 = new JSONObject();
        json1.put("e", new JSONObject().put("f", "{$current_date} {$current_date}"));
        json1.put("g", new JSONObject().put("j", new JSONObject().put("f", "{$current_date} {$current_date}")));
        JSONObject json2 = new JSONObject();
        json2.put("k", new JSONArray().put(new JSONObject().put("f", "{$current_date} {$current_date}")));
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(json1).put(json2);

        JSONArray result = jsonFaker.fakeData(jsonArray);

        assertFalse(result.toString().contains("{$"));
    }

    @Test
    public void testFakeUid() {
        JSONObject json = new JSONObject();
        json.put("b", new JSONObject().put("c", "{$uid}"));

        JSONObject result = jsonFaker.fakeData(json);

        String uid = result.getJSONObject("b").getString("c");
        assertTrue(uid.matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"));
    }

    @Test
    public void testFakeCaller() {
        JSONObject json = new JSONObject();
        json.put("b", new JSONObject().put("c", "{$caller}"));

        JSONObject result = jsonFaker.fakeData(json);

        String caller = result.getJSONObject("b").getString("c");
        assertEquals(caller, "JsonFakerTest.testFakeCaller");
    }

    @Test
    public void testCallerFakerOnClassInitialization() {
        jsonFaker.fakeData(json).similar(new JSONObject().put("f", "JsonFakerTest.<init"));
    }

    @Test
    public void testFakeDate() {
        String date = DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime());
        JSONObject json = new JSONObject();
        json.put("b", new JSONObject().put("c", "{$current_date}"));

        JSONObject result = jsonFaker.fakeData(json);

        String dt = result.getJSONObject("b").getString("c");
        assertEquals(dt, date);
    }

    @Test
    public void testFakeDateTime() {
        String date = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").print(new DateTime());
        JSONObject json = new JSONObject();
        json.put("b", new JSONObject().put("c", "{$current_datetime}"));

        JSONObject result = jsonFaker.fakeData(json);

        String dt = result.getJSONObject("b").getString("c");
        assertTrue(dt.contains(date.substring(0, 15)), date);
    }

    @Test
    public void testFakeWithUnsupportedParameter() {
        JSONObject json = new JSONObject();
        json.put("b", new JSONObject().put("c", "{$incorrect}"));

        boolean passed;
        try {
            jsonFaker.fakeData(json);
            passed = true;
        } catch (IllegalArgumentException ex) {
            passed = false;
            assertEquals(ex.getMessage(), "No enum constant com.mbi.fakers.SupportedParameters.INCORRECT");
        }
        assertFalse(passed);
    }

    @Test
    public void testFakeWithInvalidParameter() {
        JSONObject json = new JSONObject();
        json.put("b", new JSONObject().put("c", "{$INVALID}"));

        boolean passed;
        try {
            jsonFaker.fakeData(json);
            passed = true;
        } catch (IllegalArgumentException ex) {
            passed = false;
            assertEquals(ex.getMessage(), "Unsupported parameter: INVALID");
        }
        assertFalse(passed);
    }

    @Test
    public void testFakeArrayOfStrings() {
        JSONArray jsonArray = new JSONArray().put("string");

        JSONArray result = jsonFaker.fakeData(jsonArray);

        result.similar(jsonArray);
    }

    @Test
    public void testFakeArrayInArray() {
        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.put(new JSONArray().put(new JSONObject().put("a", "{$uid}")));

        JSONArray result = jsonFaker.fakeData(jsonArray1);

        assertFalse(result.toString().contains("{$"));
        String uid = result.getJSONArray(0).getJSONObject(0).getString("a");
        assertTrue(uid.matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"));
    }

    @Test
    public void testIntIsNotConvertedToString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", 1);

        assertEquals(jsonFaker.fakeData(jsonObject).toString(), jsonObject.toString());
    }
}
