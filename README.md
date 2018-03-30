# WCEHackathon_SupplyChain
Hackathon Project for WCE Hackathon (https://wcehackathon.in/)

Project Abstract :-
                For the past few years, food safety and traceability in trading market of farming has become and Crucial problem in India. Since traditional agri-food Logistics pattern cannot match the demands of the market and customer anymore, building an agri-food supply chain traceability system is becoming more and more urgent. In this system, we implement utilization and development situation of RFID (Radio-Frequency Identification) and Blockchain technology for bringing transparency in trading in farm products and delivering quality products to customers. It can realize the traceability with trusted information in the entire agri-food supply chain, which would effectively guarantee the food safety, by gathering, transferring and sharing the authentic data of agri-food in production, processing, warehousing, distribution and selling links. And also able to eliminate some middle chain members from chain which will lead to more benefit for customer by making product price low than usual.

Tips:-
This repo contains server side programm for running blockchain on nodes.
for configuring nodes look into config.java and respective fields. if you are running this project please remember this is prototype and it can run on 25 - 30  nodes. I havn't tried it on full scale yet.

Database:- You will need MongoDB to save data from blockchain. create db called 'blockchain' in the MongoDB and collection called 'blocks'.
