: refresh_token
@curl^
    --silent^
    --header "Authorization Basic YWRtaW46cGFzc3dvcmQ="^
    "http://localhost:8080/oauth/token?grant_type=password&client_id=my-trusted-client&username=admin&password=password"^
    > refresh.json
@type refresh.json
@echo.
@type refresh.json | jq -r ".value" > access_token.txt
@set /p access_token=<access_token.txt
@goto rest_call
: access_token
@rem curl^
    --silent^
    --header "Authorization Basic YWRtaW46cGFzc3dvcmQ="^
    "http://admin:password@localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=%access_token%&client_id=my-trusted-client"^
    > access.json
@rem type access.json
@echo.
:rest_call
@curl^
    --silent^
    "http://localhost:8080/restapi/locations?access_token=%access_token%"^
    > locations.json
@type locations.json
@echo.
@del locations.json
@del refresh.json
@del access.json
@del access_token.txt