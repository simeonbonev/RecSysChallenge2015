function addSessionDuration() {
    print('Adding sessionDuration');
    db.clicksAggregated.find().forEach(
        function (elem) {
            db.clicksAggregated.update(
                {
                    _id: elem._id
                },
                {
                    $set: {
                        sessionDuration: ((ISODate(elem.sessionEnd).getTime() - ISODate(elem.sessionStart).getTime())/1000)
                    }
                }
            );
        }
    );
}