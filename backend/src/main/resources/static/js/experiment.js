const API_MAT = "/material";
const API_CALC = "/calculate";

let chart1 = null;
let chart2 = null;

document.addEventListener("DOMContentLoaded", () => {
    loadMaterials();
    document.getElementById("calcBtn").addEventListener("click", calculateAndPlot);
});

async function loadMaterials() {
    try {
        const res = await fetch(API_MAT);
        const materials = await res.json();
        materialsList = materials;
        const sel = document.getElementById("materials");

        materials.forEach(m => {
            const opt = document.createElement("option");
            opt.value = m.material_id;
            opt.textContent = m.material_name;
            sel.appendChild(opt);
        });
    } catch (err) {
        console.error("Ошибка загрузки материалов:", err);
    }
}

function getInputValue(id, name) {
    const val = parseFloat(document.getElementById(id).value);
    if (isNaN(val)) throw new Error(`${name} не задано или некорректно`);
    return val;
}

async function calculateDensity(material_id, Pg, T) {
    const res = await fetch(API_CALC, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            material_id: parseInt(material_id),
            Pg: parseFloat(Pg),
            T: parseFloat(T)
        })
    });
    const data = await res.json();
    return data.p;
}

function range(start, end, step) {
    const result = [];
    for (let i = 0; i <= Math.floor((end - start)/step); i++) {
        result.push(parseFloat((start + i*step).toFixed(10)));
    }
    return result;
}

function createTable(labels, datasets, title, firstColumnName) {
    const container = document.createElement("div");
    container.classList.add("table-container");

    const heading = document.createElement("h3");
    heading.textContent = title;
    container.appendChild(heading);

    const table = document.createElement("table");
    table.classList.add("data-table");

    const thead = document.createElement("thead");
    const headerRow = document.createElement("tr");
    const firstHeader = document.createElement("th");
    firstHeader.textContent = firstColumnName;
    headerRow.appendChild(firstHeader);
    datasets.forEach(ds => {
        const th = document.createElement("th");
        th.textContent = ds.label;
        headerRow.appendChild(th);
    });
    thead.appendChild(headerRow);
    table.appendChild(thead);

    const tbody = document.createElement("tbody");
    for (let i = 0; i < labels.length; i++) {
        const tr = document.createElement("tr");
        const tdLabel = document.createElement("td");
        tdLabel.textContent = labels[i];
        tr.appendChild(tdLabel);

        datasets.forEach(ds => {
            const td = document.createElement("td");
            td.textContent = ds.data[i].toFixed(3);
            tr.appendChild(td);
        });

        tbody.appendChild(tr);
    }

    table.appendChild(tbody);
    container.appendChild(table);

    return container;
}

function exportTableToCSV(table, filename, materialName) {
    const rows = Array.from(table.querySelectorAll("tr"));

    let csvContent = `Материал;${materialName}\n`;

    csvContent += rows.map(tr => {
        const cols = Array.from(tr.querySelectorAll("th, td"));
        return cols.map(td => td.textContent).join(";");
    }).join("\n");

    const blob = new Blob(["\uFEFF" + csvContent], { type: "text/csv;charset=utf-8;" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.setAttribute("download", filename);
    document.body.appendChild(link);
    link.click();
    link.remove();
}


async function calculateAndPlot() {
    const timeStart = performance.now();
    let materialId, Pgmin, Pgmax, Pgstep, Tmin, Tmax, Tstep;
    try {
        materialId = parseInt(document.getElementById("materials").value);
        Pgmin = getInputValue("pgmin", "Минимальное давление");
        Pgmax = getInputValue("pgmax", "Максимальное давление");
        Pgstep = getInputValue("pgstep", "Шаг давления");
        Tmin = getInputValue("tmin", "Минимальная температура");
        Tmax = getInputValue("tmax", "Максимальная температура");
        Tstep = getInputValue("tstep", "Шаг температуры");

        if (Pgmin < 0) throw new Error("Минимальное давление не может быть отрицательным");
            if (Pgmax < 0) throw new Error("Максимальное давление не может быть отрицательным");
            if (Pgstep <= 0) throw new Error("Шаг давления должен быть > 0");
            if (Tstep <= 0) throw new Error("Шаг температуры должен быть > 0");
            if (Pgmax <= Pgmin) throw new Error("Максимальное давление должно быть больше минимального");
            if (Tmax <= Tmin) throw new Error("Максимальная температура должна быть больше минимальной");
    } catch (err) {
        alert(err.message);
        return;
    }

    const pressures = range(Pgmin, Pgmax, Pgstep);
    const temperatures = [Tmin, (Tmin + Tmax)/2, Tmax];
    const tempColors = ['blue','green','red'];
    const datasets1 = [];

    for (let i = 0; i < temperatures.length; i++) {
        const T = temperatures[i];
        const densities = await Promise.all(pressures.map(Pg =>
            calculateDensity(materialId, Pg, T)
        ));
        datasets1.push({
            label: `T,°C = ${T}`,
            data: densities,
            borderColor: tempColors[i],
            fill: false
        });
    }

    const ctx1 = document.getElementById('chart1').getContext('2d');
    if (chart1) chart1.destroy();
    chart1 = new Chart(ctx1, {
        type: 'line',
        data: { labels: pressures, datasets: datasets1 },
        options: {
            responsive: true,
            plugins: {
                title: { display: true, text: 'Плотность vs Температура' },
                tooltip: {
                    callbacks: {
                        label: function(ctx) {
                            return `ρ = ${ctx.parsed.y.toFixed(3)} г/см³`;
                        }
                    }
                }
            },
            scales: {
                x: { title: { display: true, text: 'Давление газа в печи, атм' } },
                y: { title: { display: true, text: 'Плотность ρ, г/см³' } }
            }
        }
    });

    const temps = range(Tmin, Tmax, Tstep);
    const pressuresToPlot = [Pgmin, (Pgmin + Pgmax)/2, Pgmax];
    const pressureColors = ['blue','green','red'];
    const datasets2 = [];

    for (let i = 0; i < pressuresToPlot.length; i++) {
        const Pg = pressuresToPlot[i];
        const densities = await Promise.all(temps.map(T =>
            calculateDensity(materialId, Pg, T)
        ));
        datasets2.push({
            label: `Pg, атм = ${Pg}`,
            data: densities,
            borderColor: pressureColors[i],
            fill: false
        });
    }

    const ctx2 = document.getElementById('chart2').getContext('2d');
    if (chart2) chart2.destroy();
    chart2 = new Chart(ctx2, {
        type: 'line',
        data: { labels: temps, datasets: datasets2 },
        options: {
            responsive: true,
            plugins: {
                title: { display: true, text: 'Плотность vs Давление' },
                tooltip: {
                    callbacks: {
                        label: function(ctx) {
                            return `ρ = ${ctx.parsed.y.toFixed(3)} г/см³`;
                        }
                    }
                }
            },
            scales: {
                x: { title: { display: true, text: 'Температура спекания, °C' } },
                y: { title: { display: true, text: 'Плотность ρ, г/см³' } }
            }
        }
    });

    let tablesContainer = document.getElementById("tables-container");
    if (!tablesContainer) {
        tablesContainer = document.createElement("div");
        tablesContainer.id = "tables-container";
        tablesContainer.classList.add("tables-container");
        document.body.appendChild(tablesContainer);
    }
    tablesContainer.innerHTML = "";
    const materialName = materialsList.find(m => m.material_id === materialId)?.material_name || "Неизвестно";

    const table1 = createTable(pressures, datasets1, "Таблица: Плотность vs Давление", "Pg, атм");
    const table2 = createTable(temps, datasets2, "Таблица: Плотность vs Температура","T,°C");

    tablesContainer.appendChild(table1);
    tablesContainer.appendChild(table2);

    // Добавляем кнопки для экспорта
    const exportBtn1 = document.createElement("button");
    exportBtn1.textContent = "Экспорт Плотность vs Давление в CSV";
    exportBtn1.addEventListener("click", () => {
        const table = table1.querySelector("table");
        exportTableToCSV(table, "density_vs_pressure.csv",materialName);
    });

    const exportBtn2 = document.createElement("button");
    exportBtn2.textContent = "Экспорт Плотность vs Температура в CSV";
    exportBtn2.addEventListener("click", () => {
        const table = table2.querySelector("table");
        exportTableToCSV(table, "density_vs_temperature.csv",materialName);
    });

    tablesContainer.appendChild(exportBtn1);
    tablesContainer.appendChild(exportBtn2);

    const timeEnd = performance.now();
        const calcTime = ((timeEnd - timeStart) / 1000).toFixed(3);

    let memoryUsed = null;
        if (performance.memory) {
            memoryUsed = (performance.memory.usedJSHeapSize / 1024 / 1024).toFixed(3); // МБ
        }
    const stats = document.createElement("p");
        stats.style.marginTop = "20px";
        stats.innerHTML = `
            <b>Время расчёта:</b> ${calcTime} с<br>
            ${memoryUsed ? `<b>Использование памяти:</b> ${memoryUsed} МБ` : ""}
        `;

    tablesContainer.appendChild(stats);
}
