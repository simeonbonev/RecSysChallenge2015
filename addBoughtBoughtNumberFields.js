function addBoughtBoughtNumber() {
    var a = db.clicksAggregatedMore.aggregate(
        [
            {
                $project: {
                    clicks: 1,
                    clicksCount: 1,
                    distinctItems: 1,
                    distinctCategories: 1,
                    timestampArray: 1,
                    sessionStart: 1,
                    sessionEnd: 1,
                    numCat: 1,
                    numItems: 1,
                    bought: {$gt: [0,1]},
                    boughtNumber: {$add: [0,0]}
                }
            },
             {
                 $out: "clicksAllFields"
             }
        ],
            {
                allowDiskUse: true
            }
    );
    printjson(a);
}