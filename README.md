[![Java CI with Gradle](https://github.com/mbi88/data-faker/actions/workflows/gradle.yml/badge.svg)](https://github.com/mbi88/data-faker/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/mbi88/data-faker/branch/master/graph/badge.svg)](https://codecov.io/gh/mbi88/data-faker)
[![Latest Version](https://img.shields.io/github/v/tag/mbi88/data-faker?label=version)](https://github.com/mbi88/data-faker/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)


# data-faker

Flexible and lightweight fake data generator for JSON objects or strings.

Supports dynamic replacement of custom parameters like `{$date}`, `{$uid}`, etc. Designed for test automation and mocking data in Java apps.

---

## Features

✅ Supports both `JSONObject` and `JSONArray`  
✅ Works with deeply nested structures  
✅ Easily extendable with new fake data types  
✅ Replaces multiple placeholders per field  
✅ Dependency-light (Joda-Time, JSON, Apache Commons)  
✅ Works great with TestNG / JUnit

---

## Supported Parameters

| Parameter          | Description                                             |
|--------------------|---------------------------------------------------------|
| `{$uid}`           | Random UUID                                             |
| `{$date}`          | Current date (`yyyy-MM-dd`)                             |
| `{$datetime}`      | Current datetime (`yyyy-MM-dd'T'HH:mm:ss`)              |
| `{$caller}`        | Caller method (e.g. `TestClass.testMethod`)             |
| `{$number}`        | Random 13-digit number (default)                        |
| `{$number;5}`      | Random number with 5 digits                             |
| `{$number;5;s}`    | Random number with 5 digits as string                   |

---

## Installation

<details>
<summary>Gradle (Kotlin DSL)</summary>

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.mbi88:data-faker:master-SNAPSHOT")
}
```

</details>

<details>
<summary>Gradle (Groovy DSL)</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.mbi88:data-faker:master-SNAPSHOT'
}
```

</details>

---

## Example

Input:

```json
{
  "field1": "Today is {$date}.",
  "field2": "Numbers: {$number}, {$number;2}",
  "field3": "{$number;2;s}",
  "meta": {
    "uuid": "{$uid}",
    "generatedAt": "{$datetime}",
    "caller": "{$caller}"
  }
}
```

Output:

```json
{
  "field1": "Today is 2025-03-24.",
  "field2": "Numbers: 1324567890123, 93",
  "field3": "05",
  "meta": {
    "uuid": "f08cd202-b51d-442e-846f-5c9a189e23a4",
    "generatedAt": "2025-03-24T21:48:13",
    "caller": "YourClass.yourMethod"
  }
}
```

---

## Usage

```java
import com.mbi.Faker;
import com.mbi.JsonFaker;
import org.json.JSONObject;

public class FakerTest {

    public static void main(String[] args) {
        Faker jsonFaker = new JsonFaker();

        JSONObject input = new JSONObject();
        input.put("uid", "{$uid}");
        input.put("now", "{$datetime}");

        JSONObject result = jsonFaker.fakeData(input);

        System.out.println(result.toString(2));
    }
}
```

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.