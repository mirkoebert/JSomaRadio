# JSomaRadio
Simple Java Soma FM Radio player

- Plays Soma FM stations
- Soma Station list configured
- Inspired from Soma Radio from Alex Kryuchkov
- Based on goxr3plus Java stream player



## Build & Run locally
### Maven
mvn clean package

java -jar target/jsomaradio.jar

### Flatpack
flatpak-builder --force-clean --user --install-deps-from=flathub --repo=repo --install builddir com.mirkoebert.JSomaRadio.yml

flatpak run  com.mirkoebert.JSomaRadio

Check flatpack build manifest   
flatpak run --command=flatpak-builder-lint org.flatpak.Builder appstream JSomaRadio.metainfo.xml

