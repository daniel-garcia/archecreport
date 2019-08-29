# archecreport


#building

mvn package

#running

java -cp target/v1export.jar com.infoblox.cto.i2o.App --api-token=$VERSION_ONE_TOKEN --api-endpoint=$VERSION_ONE_ENDPOINT -f /tmp/query.json

# example query.json
```json
{
  "from": "Epic",
  "select": ["Name"]
}
```
