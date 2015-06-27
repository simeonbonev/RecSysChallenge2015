function addBuyStatus() {
    print('Adding sessionDuration');
    var p = 0;
    db.buys.find().forEach(
        function (elem) {
            p++;
            if(p%1000 === 0) {
                print("PROGRESS: ", p);
            }
            db.clicksAllFields.update(
                {
                    _id: elem.sessionId
                },
                {
                    $set: {
                        bought: true
                    },
                    $inc: {
                        boughtNumber: 1
                    }
                }
            );
        }
    );
}