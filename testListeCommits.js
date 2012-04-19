Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/";

test("Checking Commits", function (done) {
        Browser.visit(home, function (e, browser) {
            expect(browser.text("title")).to.be("CodeStory - HomePage");
            expect(browser.query("#commits .commit:nth-child(1):contains('dgageot')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2):contains('jlm')")).to.be.ok();
            done();
        });
    }
);
