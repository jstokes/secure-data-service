#!/bin/bash

mongo sli --eval 'db.adminCommand("listDatabases").databases.forEach( function (d) {if (d.name != "local" && d.name != "admin" && d.name != "config") db.getSiblingDB(d.name).dropDatabase();})'

mongo sli ../indexes/sli_indexes.js
mongo is ../indexes/is_indexes.js
mongo ingestion_batch_job ../indexes/ingestion_batch_job_indexes.js
mongo 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a ../indexes/tenantDb_indexes.js
mongo d36f43474916ad310100c9711f21b65bd8231cc6 ../indexes/tenantDb_indexes.js
mongo f25ce1b8a399bd8621a57427a20039b4b13935db ../indexes/tenantDb_indexes.js
mongo eb962e0ee6c86d75b55e8f861737c50ca308e021 ../indexes/tenantDb_indexes.js
