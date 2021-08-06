#!/bin/bash

function patch_database() {
    echo 'execute all database patches:'
    for f in /opt/argus/sql/patch/*; do
        echo "    applying patch $f"
        mysql --user='root' < $f
    done
    echo 'patching done'
}

function deploy_database() {
    echo 'deploy /opt/argus/sql/schema.sql'
    mysql --user='root' < /opt/argus/sql/schema.sql

    #echo 'deploy /opt/argus/sql/versioning.sql'
    #mysql --user='root' < /opt/argus/sql/versioning.sql

}

function insert_demo_data() {
    echo 'deploy /opt/argus/sql/demo_data.sql'
    mysql --user='root' < /opt/argus/sql/demo_data.sql
}

if [ $(mysqlshow | grep -c argus) -eq 1 ] && [ $(mysqlshow | grep -c versioning) -eq 1 ]; then
        echo "argus and versioning exists"
        echo "apply any required patches"
        patch_database
else
    if [ $(mysqlshow | grep -c argus) -eq 1 ]; then
            echo "argus exists but it's not versioned, manual intervention required";
            echo "use the migration scripts to bring the database in a suported format ";
            echo "argus/db/migrate/";
            echo "then initialize versioning table";
            exit 1
    else
            echo "Table argus does not exist"
            echo "initializing setup with a fresh database"
            deploy_database

            echo "apply any required patches"
            patch_database

            echo "deploy demo data"
            insert_demo_data
    fi
fi
