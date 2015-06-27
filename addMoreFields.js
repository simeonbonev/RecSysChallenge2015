function addMoreFields() {
    var a = db.clicksAggregated.aggregate(
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
                    numCat: {$size: "$distinctCategories"},
                    numItems: {$size: "$distinctItems"},
                }
            },
            {
                $out: "clicksAggregatedMore"
            }
        ],
            {
                allowDiskUse: true
            }
    );
    printjson(a);
}