{"id":24307,"url":"https://www.tvmaze.com/shows/24307/el-chavo-del-ocho","name":"El Chavo del Ocho","type":"Scripted","language":"Spanish","genres":["Drama","Comedy","Adventure"],"status":"Ended","runtime":30,"averageRuntime":30,"premiered":"1973-04-09","officialSite":null,"schedule":{"time":"","days":["Monday"]},"rating":{"average":null},"weight":13,"network":{"id":1480,"name":"Telesistema Mexicano","country":{"name":"Mexico","code":"MX","timezone":"America/Monterrey"}},"webChannel":null,"dvdCountry":null,"externals":{"tvrage":null,"thetvdb":82516,"imdb":"tt0229889"},"image":{"medium":"https://static.tvmaze.com/uploads/images/medium_portrait/92/230988.jpg","original":"https://static.tvmaze.com/uploads/images/original_untouched/92/230988.jpg"},"summary":"<p><b>El Chavo del 8 </b>is the history of a Mexican boy (el Chavo) who lives in a vicinity of the City of Mexico within a barrel located in the patio of the vicinity. The program explores, of humorous way, the tangles that the childrens get into, especially el chavo with their friends and neighbors like kiko, chilindrina, Don Ramon, among others.</p>","updated":1573149811,"_links":{"self":{"href":"https://api.tvmaze.com/shows/24307"},"previousepisode":{"href":"https://api.tvmaze.com/episodes/1035642"}}}

db.series.insert({"id":24307,"url":"https://www.tvmaze.com/shows/24307/el-chavo-del-ocho","name":"El Chavo del Ocho","type":"Scripted","language":"Spanish","genres":["Drama","Comedy","Adventure"],"status":"Ended","runtime":30,"averageRuntime":30,"premiered":"1973-04-09","officialSite":null,"schedule":{"time":"","days":["Monday"]},"rating":{"average":null},"weight":13,"network":{"id":1480,"name":"Telesistema Mexicano","country":{"name":"Mexico","code":"MX","timezone":"America/Monterrey"}},"webChannel":null,"dvdCountry":null,"externals":{"tvrage":null,"thetvdb":82516,"imdb":"tt0229889"},"image":{"medium":"https://static.tvmaze.com/uploads/images/medium_portrait/92/230988.jpg","original":"https://static.tvmaze.com/uploads/images/original_untouched/92/230988.jpg"},"summary":"<p><b>El Chavo del 8 </b>is the history of a Mexican boy (el Chavo) who lives in a vicinity of the City of Mexico within a barrel located in the patio of the vicinity. The program explores, of humorous way, the tangles that the childrens get into, especially el chavo with their friends and neighbors like kiko, chilindrina, Don Ramon, among others.</p>","updated":1573149811,"_links":{"self":{"href":"https://api.tvmaze.com/shows/24307"},"previousepisode":{"href":"https://api.tvmaze.com/episodes/1035642"}}})

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
