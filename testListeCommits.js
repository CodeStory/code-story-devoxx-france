Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/";

test("Checking Page Title", function (done) {
        Browser.visit(home, function (e, browser) {
            expect(browser.text("title")).to.be("CodeStory - HomePage");
            done();
        });
    }
);

test("Checking Commits", function (done) {
        Browser.visit(home, function (e, browser) {
            expect(browser.query("#commits .commit:nth-child(1):contains('dgageot')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2):contains('jlm')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) .message:contains('last commit')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) .message:contains('first commit')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) .date:contains('29/03/2012')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) .date:contains('29/03/2012')")).to.be.ok();

            done();
        });
    }
);
