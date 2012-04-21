Browser = require("zombie");
expect = require("expect.js");

home = "http://localhost:" + process.env.PORT + "/";

test("Checking page title", function (done) {
    Browser.visit(home, function (e, browser) {
        expect(browser.text("title")).to.be("CodeStory - HomePage");
        done();
    });
});

test("Checking commits", function (done) {
    Browser.visit(home, function (e, browser) {
        expect(browser.query("#commits .commit:nth-child(1) :contains('message1')")).to.be.ok();
        expect(browser.query("#commits .commit:nth-child(1) img[src='url1']")).to.be.ok();
        expect(browser.query("#commits .commit:nth-child(1) img[class='portrait SUCCESS']")).to.be.ok();

        expect(browser.query("#commits .commit:nth-child(2) :contains('message2')")).to.be.ok();
        expect(browser.query("#commits .commit:nth-child(2) img[src='url2']")).to.be.ok();
        expect(browser.query("#commits .commit:nth-child(2) img[class='portrait FAILURE']")).to.be.ok();

        done();
    });
});

test("Checking project name", function (done) {
    Browser.visit(home, function (e, browser) {
        expect(browser.query("h2:contains('CodeStory')")).to.be.ok();
        done();
    });
});

test("Checking badges", function (done) {
    Browser.visit(home, function (e, browser) {
        expect(browser.query("#badges .badge:nth-child(1) :contains('Top Committer')")).to.be.ok();
        expect(browser.query("#badges .badge:nth-child(1) img[src='/badges/top.png']")).to.be.ok();

        expect(browser.query("#badges .badge:nth-child(2) :contains('Fatty Committer')")).to.be.ok();
        expect(browser.query("#badges .badge:nth-child(2) img[src='/badges/fatty.png']")).to.be.ok();

        expect(browser.query("#badges .badge:nth-child(3) :contains('Verbose Committer')")).to.be.ok();
        expect(browser.query("#badges .badge:nth-child(3) img[src='/badges/verbose.png']")).to.be.ok();

        done();
    });
});

