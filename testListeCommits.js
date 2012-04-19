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
            expect(browser.query("#commits .commit:nth-child(1):contains('author1')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2):contains('author2')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) .message:contains('message1')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) .message:contains('message2')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) .date:contains('date1')")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) .date:contains('date2')")).to.be.ok();

            expect(browser.query("#commits .commit:nth-child(1) img[src='url1']")).to.be.ok();
            expect(browser.query("#commits .commit:nth-child(2) img[src='url2']")).to.be.ok();

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
