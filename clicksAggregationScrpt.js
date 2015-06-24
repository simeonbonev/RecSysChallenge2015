db.sampleclicks.aggregate(
   [
     { $group : { _id : "$sessionId", clicks: { $push: {timestamp: "$timestamp", itemId: "$itemId", category: "$category"} } } }
   ]
)
