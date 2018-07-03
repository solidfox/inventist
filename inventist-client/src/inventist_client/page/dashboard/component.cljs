(ns inventist-client.page.dashboard.component
  (:require [remodular.core :refer [modular-component]]
            [symbols.detailview :as s-detailview]
            [symbols.general :as s-general]
            [inventist-client.page.dashboard.core :as core]
            [view-dashboard.component :refer [dashboard-detail dashboard-stats]]
            [symbols.color :as color]
            [rum.core :refer [defc]]))

(def applescript "applescript://com.apple.scripteditor?action=new&script=--%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%20%20%20%0D--%20%20%20%20%20%20%20%20%20%20%20.%3A%3B%3A.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%3A%3B%3A.%20%20%0D--%20%20%20%20%20%20%20%20%20.%3A%3B%3B%3B%3B%3B%3A.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%3A%3B%3B%3B%3B%3B%3A.%0D--%20%20%20%20%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20__%20%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%0D--%20%20%20%20%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%2F%20%20%7C%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%0D--%20%20%20%20%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%60%7C%20%7C%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%0D--%20%20%20%20%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%7C%20%7C%20%20%20%20%20%20%20%3B%3B%3B%3B%3B%20%20%0D--%20%20%20_____%20%20%20%3B%3A%3B%3B%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20_%7C%20%7C_%20%20%20%20%20%20%3B%3A%3B%3B%3B%20%20%0D--%20%20%2F%20__%20%20%5C%20%20%3A%20%3B%3B%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%5C___%2F%20%20%20%20%20%20%3A%20%3B%3B%3B%20%20%0D--%20%20%60'%20%2F%20%2F'%20%20%20%20%3B%3A%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%3B%3A%3B%20%20%0D--%20%20%20%20%2F%20%2F%20%20%20%20.%20%3A.%3B%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%3A.%3B%20%20%0D--%20%20.%2F%20%2F___%20%20%20%20.%20%3A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%3A%20%20%0D--%20%20%5C_____%2F%20%20.%20%20%20.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%20%20.%20%20%0D--%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%0D--%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20.%20%20%20%0D--%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%201%EF%B8%8F⃣%20Klicka%20först%20på%20%22Nytt%20Skript%22%0D--%0D--%202%EF%B8%8F⃣%20Klicka%20sedan%20på%20play-knappen%20här%20ovanför%20och%20följ%20instruktionerna%20%0D--%20%20%20%20(knappen%20ser%20ut%20såhär%3A%20▶%EF%B8%8E)%0D--%0D--%0D--%203%EF%B8%8F⃣%20När%20du%20registrerat%20din%20dator%20kan%20du%20stänga%20detta%20fönster.%20Du%20behöver%20inte%20spara.%20\uD83D\uDC4D\uD83C\uDFFC%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D%0D--%20Get%20desired%20info%0Dset%20ethernetAddress%20to%20primary%20Ethernet%20address%20of%20(system%20info)%0Dset%20osVersion%20to%20system%20version%20of%20(system%20info)%0Dtell%20application%20%22Finder%22%0D%09set%20gigabyte%20to%201.0E%2B9%0D%09set%20diskCapacity%20to%20(capacity%20of%20startup%20disk)%20%2F%20gigabyte%20as%20integer%0D%09set%20usedDisk%20to%20((diskCapacity)%20-%20(free%20space%20of%20startup%20disk))%20%2F%20gigabyte%20as%20integer%0D%09set%20freeDisk%20to%20(free%20space%20of%20startup%20disk)%20%2F%20gigabyte%20as%20integer%0D%09set%20diskName%20to%20name%20of%20startup%20disk%0Dend%20tell%0Dset%20smartStatus%20to%20do%20shell%20script%20%22diskutil%20info%20%5C%22%22%20%26%20diskName%20%26%20%22%5C%22%20%7C%20grep%20SMART%20%7C%20xargs%22%0Dset%20solidStateDrive%20to%20(do%20shell%20script%20%22diskutil%20info%20%5C%22%22%20%26%20diskName%20%26%20%22%5C%22%20%7C%20egrep%20'Solid%20State%3A%5C%5Cs%2BYes'%20%7C%20wc%20-l%22)%20as%20integer%20as%20boolean%0Dset%20ram%20to%20physical%20memory%20of%20(system%20info)%0D%0D--%20Set%20default%20answers%0Dset%20userFirstName%20to%20do%20shell%20script%20%22finger%20%24(whoami)%20%7C%20awk%20'%2FName%3A%2F%20%7Bprint%20%244%7D'%22%0Dset%20userLastName%20to%20do%20shell%20script%20%22finger%20%24(whoami)%20%7C%20awk%20'%2FName%3A%2F%20%7Bprint%20%245%7D'%22%0Dset%20birthday%20to%20%22%22%0Dset%20lastFour%20to%20%22XXXX%22%0Dset%20userKnowsPassword%20to%20%22TRUE%22%0D%0Dset%20questionIndex%20to%200%0Dset%20nQuestions%20to%204%0Dset%20backButton%20to%20%22Backa%22%0Dset%20cancelButton%20to%20%22Avbryt%22%0Dset%20defaultButtons%20to%20%7BbackButton%2C%20cancelButton%2C%20%22OK%22%7D%0D%0Drepeat%20while%20questionIndex%20≤%20nQuestions%0D%09--try%0D%09if%20questionIndex%20%3D%200%20then%0D%09%09set%20answer%20to%20choose%20from%20list%20getSchoolClasses()%20with%20prompt%20%22Välj%20din%20klass%22%0D%09%09if%20answer%20is%20not%20false%20then%0D%09%09%09set%20schoolClass%20to%20item%201%20of%20answer%0D%09%09else%0D%09%09%09error%0D%09%09end%20if%0D%09else%0D%09%09set%20dialogAnswer%20to%20%7B%7D%0D%09%09if%20questionIndex%20%3D%201%20then%0D%09%09%09%0D%09%09%09set%20dialogAnswer%20to%20display%20dialog%20¬%0D%09%09%09%09%22Vad%20är%20ditt%20förnamn%3F%22%20default%20answer%20userFirstName%20¬%0D%09%09%09%09buttons%20defaultButtons%20default%20button%20%22OK%22%20cancel%20button%20cancelButton%0D%09%09%09set%20userFirstName%20to%20text%20returned%20of%20result%0D%09%09%09%0D%09%09else%20if%20questionIndex%20%3D%202%20then%0D%09%09%09%0D%09%09%09set%20dialogAnswer%20to%20display%20dialog%20¬%0D%09%09%09%09%22Vad%20är%20ditt%20efternamn%3F%22%20default%20answer%20userLastName%20¬%0D%09%09%09%09buttons%20defaultButtons%20default%20button%20%22OK%22%20cancel%20button%20cancelButton%0D%09%09%09set%20userLastName%20to%20text%20returned%20of%20result%0D%09%09%09%0D%09%09else%20if%20questionIndex%20%3D%203%20then%0D%09%09%09%0D%09%09%09set%20prompt%20to%20%22Ange%20din%20födelsedag%20på%20formen%20ÅÅÅÅ-MM-DD%20t%20ex%202000-01-31%22%0D%09%09%09set%20reprompt%20to%20%22Felaktigt%20inmatad%20födelsedag.%20Ange%20din%20födelsedag%20på%20formen%20ÅÅÅÅ-MM-DD%20t%20ex%202000-01-31%22%0D%09%09%09set%20goingBack%20to%20false%0D%09%09%09set%20triedOnce%20to%20false%0D%09%09%09repeat%20while%20¬%0D%09%09%09%09not%20triedOnce%20¬%0D%09%09%09%09%09or%20(not%20matchRegEx(birthday%2C%20%22%5C%5Cd%5C%5Cd%5C%5Cd%5C%5Cd%5C%5C-%5C%5Cd%5C%5Cd%5C%5C-%5C%5Cd%5C%5Cd%22)%20¬%0D%09%09%09%09and%20not%20goingBack)%0D%09%09%09%09set%20dialogAnswer%20to%20display%20dialog%20prompt%20default%20answer%20birthday%20buttons%20defaultButtons%20default%20button%20%22OK%22%20cancel%20button%20cancelButton%0D%09%09%09%09set%20birthday%20to%20text%20returned%20of%20dialogAnswer%0D%09%09%09%09set%20goingBack%20to%20button%20returned%20of%20dialogAnswer%20%3D%20backButton%0D%09%09%09%09set%20prompt%20to%20reprompt%0D%09%09%09%09set%20triedOnce%20to%20true%0D%09%09%09end%20repeat%0D%09%09%09%0D%09%09else%20if%20questionIndex%20%3D%204%20then%0D%09%09%09%0D%09%09%09set%20dialogAnswer%20to%20display%20dialog%20%22Om%20du%20kan%20dem%2C%20ange%20de%20fyra%20sista%20siffrorna%20i%20ditt%20personnummer%22%20default%20answer%20lastFour%20buttons%20%7BbackButton%2C%20cancelButton%2C%20%22OK%22%7D%20default%20button%20%22OK%22%20cancel%20button%20cancelButton%0D%09%09%09set%20lastFour%20to%20text%20returned%20of%20dialogAnswer%0D%09%09%09%0D%09%09end%20if%0D%09%09if%20button%20returned%20of%20dialogAnswer%20%3D%20backButton%20then%0D%09%09%09set%20questionIndex%20to%20questionIndex%20-%202%0D%09%09end%20if%0D%09end%20if%0D%09set%20questionIndex%20to%20questionIndex%20%2B%201%0Dend%20repeat%0D%0Dset%20diagnostics%20to%20%22%22%20--do%20shell%20script%20%22ps%20-axv%22%0D%0D--%20Uppdatera%20datorns%20namn%20(för%20att%20undvika%20att%20alla%20datorer%20på%20nätet%20heter%20samma%20sak)%0Ddisplay%20dialog%20%22Systemet%20kommer%20nu%20fråga%20om%20ditt%20lösenord%20till%20datorn%20för%20att%20göra%20lite%20inställningar.%22%0Dset%20userName%20to%20userFirstName%20%26%20%22%20%22%20%26%20userLastName%0Dtry%0D%09setComputerName(userName%2C%20getSerialNumber())%0Don%20error%20errorMessage%20number%20errorNumber%0D%09set%20diagnostics%20to%20errorMessage%0D%09if%20errorNumber%20%3D%20-128%20then%20--%20User%20canceled%0D%09%09set%20userKnowsPassword%20to%20%22FALSE%22%0D%09end%20if%0Dend%20try%0D%0D--%20Skicka%20uppgifterna%20till%20Google%20Docs%0Dset%20response%20to%20do%20shell%20script%20%22curl%22%20%26%20¬%0D%09%22%20-F%20'entry.159483965%3D%22%20%26%20userFirstName%20%26%20¬%0D%09%22'%20-F%20'entry.1044585513%3D%22%20%26%20userLastName%20%26%20¬%0D%09%22'%20-F%20'entry.906523132%3D%22%20%26%20schoolClass%20%26%20¬%0D%09%22'%20-F%20'entry.934895511%3D%22%20%26%20getSerialNumber()%20%26%20¬%0D%09%22'%20-F%20'entry.1412871346%3D%22%20%26%20ethernetAddress%20%26%20¬%0D%09%22'%20-F%20'entry.1179188703%3D%22%20%26%20getModel()%20%26%20¬%0D%09%22'%20-F%20'entry.2076120642%3D%22%20%26%20osVersion%20%26%20¬%0D%09%22'%20-F%20'entry.441750522%3D%22%20%26%20diskCapacity%20%26%20¬%0D%09%22'%20-F%20'entry.613034536%3D%22%20%26%20freeDisk%20%26%20¬%0D%09%22'%20-F%20'entry.47357944%3D%22%20%26%20diskName%20%26%20¬%0D%09%22'%20-F%20'entry.746140863%3D%22%20%26%20smartStatus%20%26%20¬%0D%09%22'%20-F%20'entry.1897553197%3D%22%20%26%20solidStateDrive%20%26%20¬%0D%09%22'%20-F%20'entry.2052951933%3D%22%20%26%20ram%20%26%20¬%0D%09%22'%20-F%20'entry.435184233%3D%22%20%26%20userKnowsPassword%20%26%20¬%0D%09%22'%20-F%20'entry.57332467%3D%22%20%26%20diagnostics%20%26%20¬%0D%09%22'%20-F%20'entry.1801148026%3D%22%20%26%20birthday%20%26%20¬%0D%09%22'%20-F%20'entry.1114297244%3D%22%20%26%20lastFour%20%26%20¬%0D%09%22'%20https%3A%2F%2Fdocs.google.com%2Fa%2Fgripsholmsskolan.se%2Fforms%2Fd%2F1PjZaYH61lWRHpW4RihJWKDVaUzFwpYMrCYdTR3r3aBM%2FformResponse%22%0D%0Dset%20registrationFailed%20to%20true%0Dif%20response%20contains%20%22Din%20dator%20är%20nu%20registrerad.%20Tack!%22%20then%0D%09set%20registrationFailed%20to%20false%0Dend%20if%0D%0D%0Dif%20not%20registrationFailed%20then%0D%09display%20dialog%20%22Klart!%20Din%20dator%20är%20nu%20registrerad%20i%20Gripsholmsskolans%20datorregister.%22%20buttons%20%7B%22Bra!%22%7D%20default%20button%20%22Bra!%22%0D%09deleteRegistrationIncentive()%0Delse%0D%09display%20dialog%20%22Registreringen%20misslyckades.%20Försök%20igen!%22%0Dend%20if%0D%0D%0Dto%20setUserName%20to%20newRealName%0D%09--%20Has%20permission%20troubles%0D%09set%20shortUserName%20to%20short%20user%20name%20of%20(system%20info)%0D%09set%20previousRealName%20to%20long%20user%20name%20of%20(system%20info)%0D%09do%20shell%20script%20%22dscl%20.%20-change%20%2FUsers%2F%22%20%26%20shortUserName%20%26%20%22%20RealName%20'%22%20%26%20previousRealName%20%26%20%22'%20'%22%20%26%20newRealName%20%26%20%22'%22%0Dend%20setUserName%0D%0Dto%20setComputerName(userName%2C%20serialNumber)%0D%09set%20computerName%20to%20(userName%20%26%20%22's%20Dator%22)%0D%09set%20hostName%20to%20serialNumber%0D%09set%20localHostName%20to%20serialNumber%0D%09do%20shell%20script%20(%22¬%0A%09sudo%20scutil%20--set%20ComputerName%20%5C%22%22%20%26%20computerName%20%26%20%22%5C%22%3B%20¬%0A%09sudo%20scutil%20--set%20HostName%20%5C%22%22%20%26%20hostName%20%26%20%22%5C%22%3B%20¬%0A%09sudo%20scutil%20--set%20LocalHostName%20%5C%22%22%20%26%20localHostName%20%26%20%22%5C%22%3B%22)%20¬%0D%09%09with%20administrator%20privileges%0Dend%20setComputerName%0D%0Dto%20matchRegEx(theString%2C%20regEx)%0D%09set%20isMatch%20to%20%220%22%20%3D%20(do%20shell%20script%20¬%0D%09%09%22egrep%20-q%20'%22%20%26%20regEx%20%26%20%22'%20%3C%3C%3C%22%20%26%20quoted%20form%20of%20theString%20%26%20%22%3B%20printf%20%24%3F%22)%0D%09return%20isMatch%0Dend%20matchRegEx%0D%0Dto%20getModel()%0D%09return%20do%20shell%20script%20%22sysctl%20hw.model%20%7C%20sed%20's%2F.*%20%2F%2F'%22%0Dend%20getModel%0D%0Dto%20getSerialNumber()%0D%09return%20do%20shell%20script%20%22ioreg%20-l%20%7C%20grep%20IOPlatformSerialNumber%20%7C%20sed%20-E%20's%2F.*%20%3D%20%5C%22(.*)%5C%22%2F%5C%5C1%2F'%22%0Dend%20getSerialNumber%0D%0Dto%20getSchoolClasses()%0D%09return%20%7B¬%0D%09%09%22Personal%22%2C%20¬%0D%09%09%22Förskolan%22%2C%20¬%0D%09%09%220%20Ekorrar%22%2C%20¬%0D%09%09%220%20Igelkottar%22%2C%20¬%0D%09%09%221%20Järvar%22%2C%20¬%0D%09%09%221%20Lodjur%22%2C%20¬%0D%09%09%222%20Isbjörnar%22%2C%20¬%0D%09%09%222%20Pingviner%22%2C%20¬%0D%09%09%223%20Örnar%22%2C%20¬%0D%09%09%223%20Ugglor%22%2C%20¬%0D%09%09%224%20Hjortar%22%2C%20¬%0D%09%09%224%20Rävar%22%2C%20¬%0D%09%09%225%20Giraffer%22%2C%20¬%0D%09%09%225%20Zebror%22%2C%20¬%0D%09%09%226%20Blåvalar%22%2C%20¬%0D%09%09%226%20Delfiner%22%2C%20¬%0D%09%09%227%20Leoparder%22%2C%20¬%0D%09%09%227%20Tigrar%22%2C%20¬%0D%09%09%228%20Björnar%22%2C%20¬%0D%09%09%228%20Vargar%22%2C%20¬%0D%09%09%22Åk%209%22%7D%0Dend%20getSchoolClasses%0D%0Dto%20deleteRegistrationIncentive()%0D%09set%20plistPath%20to%20%22~%2FLibrary%2FLaunchAgents%2Fse.gripsholmsskolan.registercomputer.plist%22%0D%09try%0D%09%09do%20shell%20script%20%22rm%20%22%20%26%20plistPath%0D%09%09do%20shell%20script%20%22launchctl%20unload%20%22%20%26%20plistPath%0D%09end%20try%0Dend%20deleteRegistrationIncentive")

(defc component < (modular-component)
  [{{state :state} :input
    trigger-event  :trigger-event}]
  [:div {:id    (str ::component-id)
         :style {:height                "100%"
                 :display               "grid"
                 :grid-template-columns "22rem auto"
                 :grid-template-rows    "100%"}}
   (dashboard-stats)

   [:div {:id    (str ::component-id)
          :style {:display            "grid"
                  :overflow           "scroll"
                  :grid-template-rows "auto 1fr"}}

    ;Toolbar
    (s-general/button {:color color/grey-normal
                       :text  "Register Device"
                       :icon  "fas fa-pen-square"
                       :on-click (fn [] (js/parent.open applescript))})
    (dashboard-detail (core/create-dashboard-detail-args state "mock-dashboard-id"))]])



