const arfolyamForm = document.getElementById("arfolyamForm");
const arfolyamTabla = document.getElementById("arfolyamTabla");
const hibaUzenet = document.getElementById("hibaUzenet");

let arfolyamChart = null;

arfolyamForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const deviza = document.getElementById("deviza").value;
    const kezdoDatum = document.getElementById("kezdoDatum").value;
    const vegDatum = document.getElementById("vegDatum").value;

    arfolyamTabla.innerHTML = "";

    hibaUzenet.classList.add("d-none");

    if (arfolyamChart !== null) {
        arfolyamChart.destroy();
        arfolyamChart = null;
    }

    if (kezdoDatum > vegDatum) {

        hibaUzenet.textContent =
            "A kezdődátum nem lehet későbbi a végdátumnál.";

        hibaUzenet.classList.remove("d-none");

        return;
    }

    const url = `/soap?deviza=${encodeURIComponent(deviza)}`
        + `&kezdoDatum=${encodeURIComponent(kezdoDatum)}`
        + `&vegDatum=${encodeURIComponent(vegDatum)}`;

    try {

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(
                "Az árfolyamlekérdezés sikertelen."
            );
        }

        const arfolyamok = await response.json();

        if (arfolyamok.length === 0) {

            hibaUzenet.textContent =
                "A megadott időszakra nem található árfolyam.";

            hibaUzenet.classList.remove("d-none");

            return;
        }

        arfolyamok.forEach(function (arfolyam) {

            const sor = document.createElement("tr");

            const datumCella = document.createElement("td");
            datumCella.textContent = arfolyam.datum;

            const devizaCella = document.createElement("td");
            devizaCella.textContent = arfolyam.deviza;

            const arfolyamCella = document.createElement("td");
            arfolyamCella.textContent = arfolyam.arfolyam;

            sor.appendChild(datumCella);
            sor.appendChild(devizaCella);
            sor.appendChild(arfolyamCella);

            arfolyamTabla.appendChild(sor);
        });

        grafikonRajzolas(arfolyamok, deviza);

    } catch (error) {

        hibaUzenet.textContent = error.message;
        hibaUzenet.classList.remove("d-none");

        console.error("MNB SOAP hiba:", error);
    }
});

function grafikonRajzolas(arfolyamok, deviza) {

    const rendezettArfolyamok = [...arfolyamok].sort(
        (a, b) => a.datum.localeCompare(b.datum)
    );

    const datumok = rendezettArfolyamok.map(
        arfolyam => arfolyam.datum
    );

    const ertekek = rendezettArfolyamok.map(
        arfolyam => arfolyam.arfolyam
    );

    const canvas = document.getElementById("arfolyamGrafikon");

    arfolyamChart = new Chart(canvas, {

        type: "line",

        data: {

            labels: datumok,

            datasets: [{

                label: `${deviza}/HUF árfolyam`,

                data: ertekek,

                borderColor: "rgb(13, 110, 253)",

                backgroundColor: "rgba(13, 110, 253, 0.1)",

                borderWidth: 2,

                tension: 0.2,

                fill: true
            }]
        },

        options: {

            responsive: true,

            scales: {

                y: {
                    title: {
                        display: true,
                        text: "Árfolyam (HUF)"
                    }
                },

                x: {
                    title: {
                        display: true,
                        text: "Dátum"
                    }
                }
            }
        }
    });
}