// CRIO_SOLUTION_AND_STUB_START_MODULE_SERIALIZATION
// CRIO_SOLUTION_AND_STUB_END_MODULE_SERIALIZATION
#!/bin/bash

lat=12.9
lng=77.8

source coordinates.txt

if test $latitude
then
    lat=$latitude
else
  echo "latitude value not set in coordinates.txt, using default"
  echo $'\e[32;1mPlease read this FAQ for more details - https://forum.crio.do/t/14751'
fi

if test $longitude
then
    lng=$longitude
else
    echo "$longitude value not set in coordinates.txt, using default"
    echo $'\e[32;1mPlease read this FAQ for more details - https://forum.crio.do/t/14751'
fi

echo -e "Please note down location coordinates which we are populating data for - \n( latitude = $lat, longitude = $lng )"
echo "If you think this is incorrect, check your coordinates.txt file."
echo $'\e[32;1mPlease read this FAQ for more details - https://forum.crio.do/t/14751'

cd ~/workspace
# Either clone or pull latest.
QEATS_SHARED_RESOURCES="${HOME}/workspace/qeats_shared_resources"
if [ ! -d $QEATS_SHARED_RESOURCES ]
then
    git clone git@gitlab.crio.do:me_qeats_shared/qeats_shared_resources.git $QEATS_SHARED_RESOURCES
else
    cd $QEATS_SHARED_RESOURCES
    git pull
fi

if systemctl status mongodb.service | grep active > /dev/null; then
    echo "MongoDB is running..."
else
    echo "MongoDB not running; Exiting"
    exit -1
fi

# Ensure a clean slate & populate all collections
mongo restaurant-database --eval "db.dropDatabase()" 
mongorestore --host localhost --db restaurant-database --gzip --archive=$QEATS_SHARED_RESOURCES/restaurants-norm-gzipped-mongo-dump

pip3 install pymongo

# Localize restaurants
echo "Localizing restaurants for your region, so that you can see them when you load the app..."
python3 $QEATS_SHARED_RESOURCES/localize_restaurants.py $lat $lng 50
