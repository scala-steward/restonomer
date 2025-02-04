# What is token request ?

The authentication mechanism in restonomer also gives the user a provision to configure the token request.

There are certain scenarios where you are required to provide the authentication credentials for the main data request
from the token value generated by some other token request.

Keeping that in mind, user can configure the token request and then can use the token response in the main data request.

User can configure whether the token needs to be fetched from token response body or token response headers.

The `token-response-placeholder` config in `token-request` can be initialised with below 2 values:
* `ResponseBody`
* `ResponseHeaders`

**_If the token is to be fetched from the token response body, it is assumed that the token request will always generate
the token response in a json format that will contain the attribute required by the main data request._**

# How to use token response in data request ?

If you want to get the `X` attribute from the token response, then you need to mention the value in the desired
field in the below format:

```text
token[X]
```

* In case of `ResponseBody`, the attribute `X` must be a dot notation of [JsonPath](https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html) for the desired token.

  Suppose, we get the below token response body:

    ```json
    {
      "data": {
        "secret": "abcd1234"
      }
    }
    ```

  In the above case, in order to get the token value of 'secret', the dot notation is `$.data.secret`

* In case of `ResponseHeaders`, the attribute `X` must be a token header name.
