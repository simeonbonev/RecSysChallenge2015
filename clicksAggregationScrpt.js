function aggregateClicks() {
    print('Aggregate clicks');
    var a = db.sampleclicks.aggregate(
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
                        distinctItems: {$addToSet: "$itemId"},
                        distinctCategories: {$addToSet: "$category"},
                        timestampArray: {$push: "$timestamp"},
                        sessionStart: {$min: "$timestamp"},
                        sessionEnd: {$max: "$timestamp"}  
                    } 
            }
           ]
        );
    printjson(a);
}
