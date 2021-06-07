{"id":45499,"url":"https://www.tvmaze.com/shows/45499/the-midnight-gospel","name":"The Midnight Gospel","type":"Animation","language":"English","genres":["Comedy","Adventure","Science-Fiction"],"status":"To Be Determined","runtime":25,"averageRuntime":26,"premiered":"2020-04-20","officialSite":"https://www.netflix.com/title/80987903","schedule":{"time":"","days":[]},"rating":{"average":7},"weight":85,"network":null,"webChannel":{"id":1,"name":"Netflix","country":null},"dvdCountry":null,"externals":{"tvrage":null,"thetvdb":378553,"imdb":"tt11639414"},"image":{"medium":"https://static.tvmaze.com/uploads/images/medium_portrait/250/625869.jpg","original":"https://static.tvmaze.com/uploads/images/original_untouched/250/625869.jpg"},"summary":"<p><b>The Midnight Gospel </b>centers on Clancy, a spacecaster with a malfunctioning multiverse simulator, who leaves the comfort of his home to interview beings living in dying worlds.</p>","updated":1599217004,"_links":{"self":{"href":"https://api.tvmaze.com/shows/45499"},"previousepisode":{"href":"https://api.tvmaze.com/episodes/1830615"}}}

db.series.insert({"id":45499,"url":"https://www.tvmaze.com/shows/45499/the-midnight-gospel","name":"The Midnight Gospel","type":"Animation","language":"English","genres":["Comedy","Adventure","Science-Fiction"],"status":"To Be Determined","runtime":25,"averageRuntime":26,"premiered":"2020-04-20","officialSite":"https://www.netflix.com/title/80987903","schedule":{"time":"","days":[]},"rating":{"average":7},"weight":85,"network":null,"webChannel":{"id":1,"name":"Netflix","country":null},"dvdCountry":null,"externals":{"tvrage":null,"thetvdb":378553,"imdb":"tt11639414"},"image":{"medium":"https://static.tvmaze.com/uploads/images/medium_portrait/250/625869.jpg","original":"https://static.tvmaze.com/uploads/images/original_untouched/250/625869.jpg"},"summary":"<p><b>The Midnight Gospel </b>centers on Clancy, a spacecaster with a malfunctioning multiverse simulator, who leaves the comfort of his home to interview beings living in dying worlds.</p>","updated":1599217004,"_links":{"self":{"href":"https://api.tvmaze.com/shows/45499"},"previousepisode":{"href":"https://api.tvmaze.com/episodes/1830615"}}})

# Busqueda
Part I
db.series.find({"status": "Ended"}).pretty()
db.series.find({"runtime": { $lt: 45 }}).pretty() 
db.series.find({"rating.average": { $lt: 9.0 }}).pretty()
db.series.find({"genres": "Science-Fiction"}).pretty()
db.series.find({ $and: [ { "genres": "Comedy" }, { "genres": "Adventure" } ]}).pretty()
db.series.find({ $and: [ { "rating.average": { $gte: 9.0 } }, { "genres": {$in: ["Crime", "Comedy"]} } ]}).pretty()
db.series.find({"genres": { $size : 3 }}).pretty()

Part II
db.series.find({"status": "Ended"}, {"name": 1}).pretty()
db.series.find({"status": "Ended"}, {"_id":0, "name": 1}).pretty()
db.series.find({"status": "Ended"}, {"_id":0, "name":1, "genres": { $slice: -1 }}).pretty()

Part III Agregations
- db.series.aggregate([{$match: {"network.name": "Adult Swim"}}, {$count: "Conteo de Adult Swim"}])

- db.series.aggregate([{ "$unwind" : "$genres" },{ "$group": { "_id": "$genres", "count": { "$sum": 1} } },{ "$group": {"_id": null,
"counts": {"$push": {"k": "$_id","v": "$count"}}} },{ "$replaceRoot": {"newRoot": { "$arrayToObject": "$counts" }} }])

- db.series.aggregate([{$match: {"language" : "English"}}, {$sort : {"rating.average": -1 }}, {$project : { "_id":0, "name": 1 } }])

Part IV Search

- db.series.find( { $text: { $search: "alcoholic grandfather" } } ).pretty()
- db.series.find( { $text: { $search: "mockumentary" } } ).pretty()


db.crewMG.insert([{"type":"Creator","person":{"id":38641,"url":"https://www.tvmaze.com/people/38641/pendleton-ward","name":"Pendleton Ward","country":{"name":"United States","code":"US","timezone":"America/New_York"},"birthday":"1982-07-08","deathday":null,"gender":"Male","image":{"medium":"https://static.tvmaze.com/uploads/images/medium_portrait/242/605869.jpg","original":"https://static.tvmaze.com/uploads/images/original_untouched/242/605869.jpg"},"_links":{"self":{"href":"https://api.tvmaze.com/people/38641"}}}},{"type":"Creator","person":{"id":38665,"url":"https://www.tvmaze.com/people/38665/duncan-trussell","name":"Duncan Trussell","country":{"name":"Canada","code":"CA","timezone":"America/Halifax"},"birthday":null,"deathday":null,"gender":"Male","image":{"medium":"https://static.tvmaze.com/uploads/images/medium_portrait/126/316463.jpg","original":"https://static.tvmaze.com/uploads/images/original_untouched/126/316463.jpg"},"_links":{"self":{"href":"https://api.tvmaze.com/people/38665"}}}}])

db.series.find({"name": "The Midnight Gospel"}, {"name": 1}).pretty()
ObjectId("60bd70269ab10f54c3962fd5")

db.crewMG.update({}, { $set: { "tvseries": new ObjectId("60bd70269ab10f54c3962fd5") } }, { "multi": true } )

db.crewMG.find({}).pretty()

db.crewMG.copyTo("crew")
db.crewMG.drop()

db.series.aggregate([{ $match: { _id: new ObjectId("60bd70269ab10f54c3962fd5") } },{ $lookup: {from: "crew", localField: "_id", foreignField: "tvseries", as: "crewSeries"} } ]).pretty()


- Conectar a servidor:
        
        ssh -p 220 uhadoop@cm.dcc.uchile.cl

- Password del servidor: 

        HADcc5212$oop