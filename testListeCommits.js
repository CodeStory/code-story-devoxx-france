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
            expect(browser.query("#commits .commit:nth-child(1):contains('jlm')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2):contains('jlm')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) .message:contains('Update README.md')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) .message:contains('removing file extensiosn')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) .date:contains('19/04/2012')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) .date:contains('29/03/2012')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) img[src='https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e']")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) img[src='https://secure.gravatar.com/avatar/649d3668d3ba68e75a3441dec9eac26e']")).to.be.ok();

            done();
        });
    }
);

test("Checking project name", function (done) {
        Browser.visit(home, function (e, browser) {
            expect(browser.query("#project-name:contains('CodeStory')")).to.be.ok();
            done();
        });
    }
);
