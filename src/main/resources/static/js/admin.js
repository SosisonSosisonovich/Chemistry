const API = "/material";

document.addEventListener("DOMContentLoaded", () => {
    loadMaterials();

    document.getElementById("addMaterialForm")
        .addEventListener("submit", addMaterial);
});
function isValidDouble(value) {
    return /^-?\d+(\.\d+)?$/.test(value.trim());
}

async function loadMaterials() {
    const res = await fetch(API);
    const materials = await res.json();

    const tbody = document.querySelector("#materialsTable tbody");
    tbody.innerHTML = "";

    materials.forEach(m => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${m.material_id}</td>
            <td><input value="${m.material_name}" id="name_${m.material_id}" /></td>

            <td><input value="${m.a0}" id="a0_${m.material_id}" /></td>
            <td><input value="${m.a1}" id="a1_${m.material_id}" /></td>
            <td><input value="${m.a2}" id="a2_${m.material_id}" /></td>
            <td><input value="${m.a3}" id="a3_${m.material_id}" /></td>
            <td><input value="${m.a4}" id="a4_${m.material_id}" /></td>
            <td><input value="${m.a5}" id="a5_${m.material_id}" /></td>

            <td>
                <button onclick="updateMaterial(${m.material_id})">Сохранить</button>
                <button onclick="deleteMaterial(${m.material_id})">Удалить</button>
            </td>
        `;

        tbody.appendChild(row);
    });
}

async function addMaterial(e) {
    e.preventDefault();

    const name = document.getElementById("name").value.trim();
    const a0 = document.getElementById("a0").value.trim();
    const a1 = document.getElementById("a1").value.trim();
    const a2 = document.getElementById("a2").value.trim();
    const a3 = document.getElementById("a3").value.trim();
    const a4 = document.getElementById("a4").value.trim();
    const a5 = document.getElementById("a5").value.trim();

    if (name.length === 0) {
        alert("Название материала не может быть пустым.");
        return;
    }

    const coeffs = [a0, a1, a2, a3, a4, a5];

    for (let i = 0; i < coeffs.length; i++) {
        if (!isValidDouble(coeffs[i])) {
            alert(`Коэффициент a${i} указан неверно.`);
            return;
        }
    }

    const body = {
        material_name: name,
        a0: Number(a0),
        a1: Number(a1),
        a2: Number(a2),
        a3: Number(a3),
        a4: Number(a4),
        a5: Number(a5)
    };

    await fetch(`${API}/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    loadMaterials();
}


async function updateMaterial(id) {
    const name = document.getElementById(`name_${id}`).value.trim();
    const a0 = document.getElementById(`a0_${id}`).value.trim();
    const a1 = document.getElementById(`a1_${id}`).value.trim();
    const a2 = document.getElementById(`a2_${id}`).value.trim();
    const a3 = document.getElementById(`a3_${id}`).value.trim();
    const a4 = document.getElementById(`a4_${id}`).value.trim();
    const a5 = document.getElementById(`a5_${id}`).value.trim();

    if (name.length === 0) {
        alert("Название материала не может быть пустым.");
        return;
    }

    const coeffs = [a0, a1, a2, a3, a4, a5];

    for (let i = 0; i < coeffs.length; i++) {
        if (!isValidDouble(coeffs[i])) {
            alert(`Коэффициент a${i} указан неверно.`);
            return;
        }
    }

    const body = {
        material_name: name,
        a0: Number(a0),
        a1: Number(a1),
        a2: Number(a2),
        a3: Number(a3),
        a4: Number(a4),
        a5: Number(a5)
    };

    await fetch(`${API}/update/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    loadMaterials();
}


async function deleteMaterial(id) {
    await fetch(`${API}/delete/${id}`, { method: "DELETE" });
    loadMaterials();
}
