package com.glen.ExcelParser.services;

public class MapInfoParserService {

}

/*
[2:35 PM] Aneer P.A
<dependency>
<groupId>org.geotools</groupId>
<artifactId>gt-shapefile</artifactId>
<version>25.2</version>
</dependency>
<dependency>
<groupId>org.geotools</groupId>
<artifactId>gt-ogr-jni</artifactId>
<version>25.2</version>
</dependency>

[2:36 PM] Aneer P.A

public List<Map<String,Object>> readMapInfoFile(File filename) throws IOException {
OGRDataStoreFactory factory = new JniOGRDataStoreFactory();
Set<String> drivers = factory.getAvailableDrivers();
for (String driver : drivers) {
// System.out.println(driver);
}
List<Map<String,Object>> retval = new ArrayList<>();
Map<String, String> connectionParams = new HashMap<String, String>();
connectionParams.put("DriverName", "MapInfo File");
connectionParams.put("DatasourceName", filename.getAbsolutePath());
DataStore store = factory.createDataStore(connectionParams);
SimpleFeatureSource featureSource = store.getFeatureSource(store.getTypeNames()[0]);
SimpleFeatureCollection features = featureSource.getFeatures();
SimpleFeatureIterator it = features.features();
try {
while (it.hasNext()) {
SimpleFeature feature = it.next();
Collection<Property> properties = feature.getProperties();
Map<String,Object> item = new HashMap<>();
properties.forEach(p -> {
item.put(p.getName().getLocalPart(),feature.getAttribute(p.getName().getLocalPart()));
});
retval.add(item);
}
} finally {
it.close();
}
return retval;
}

*/