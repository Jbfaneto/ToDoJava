const tasksEndpoint = "http://localhost:8080/tasks/user";

function hideLoader() {
  document.getElementById("loading").style.display = "none";
}

function show(tasks) {
  console.log(tasks);
  let tab = `<thead>
            <th scope="col">#</th>
            <th scope="col">Description</th>
        </thead>`;

  for (let task of tasks) {
    tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td>${task.description}</td>
            </tr>
        `;
      
        console.log(task);
  }

  document.getElementById("tasks").innerHTML = tab;
}

async function getTasks() {
  let key = "Authorization";
  try {
    const response = await fetch(tasksEndpoint, {
      method: "GET",
      headers: new Headers({
        Authorization: localStorage.getItem(key),
      }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    var data = await response.json();
    console.log(data);
    if (response) hideLoader();
    show(data);
  } catch (error) {
    console.log('Fetch error: ', error);
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (!localStorage.getItem("Authorization"))
    window.location = "/view/login.html";
});

getTasks();