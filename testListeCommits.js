Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:8080/";

test("Checking Commits", function (done) {
        Browser.visit(home, function (e, browser) {
            expect(browser.text("title")).to.be("CodeStory - HomePage");
            expect(browser.query("#commits")).to.be.ok();
            done();
        });
    }
);
