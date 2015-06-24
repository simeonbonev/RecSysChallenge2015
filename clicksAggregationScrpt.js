function aggregateClicks() {
    print('Aggregate clicks');
    printjson(
        db.sampleclicks.aggregate(
           [
             { 
                $group : 
                    { 
                        _id : "$sessionId", 
                        clicks: { 
                            $push: {
                                timestamp: "$timestamp", 
                                itemId: "$itemId", 
                                category: "$category"
                            } 
                        },
                        clicksCount: {$sum: 1},
                        distinctItems: {$addToSet: "$itemId"}  
                    } 
            }
           ]
        )
    );
}
