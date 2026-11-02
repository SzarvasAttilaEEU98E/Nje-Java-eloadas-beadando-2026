document.addEventListener("DOMContentLoaded", async function () {

    try {

        const response = await fetch("/forex/account/summary");

        if (!response.ok) {
            throw new Error(
                "Az OANDA számlaadatok lekérdezése sikertelen."
            );
        }

        const account = await response.json();

        document.getElementById("accountId").textContent =
            account.id;

        document.getElementById("currency").textContent =
            account.currency;

        document.getElementById("balance").textContent =
            account.balance + " " + account.currency;

        document.getElementById("nav").textContent =
            account.nav + " " + account.currency;

        document.getElementById("marginAvailable").textContent =
            account.marginAvailable + " " + account.currency;

        document.getElementById("openTradeCount").textContent =
            account.openTradeCount;

        document.getElementById("openPositionCount").textContent =
            account.openPositionCount;

    } catch (error) {

        console.error("OANDA számlaadatok hiba:", error);

        alert(error.message);
    }
});