// frontend/src/main/resources/static/scriptUsers.js

// Función para manejar el inicio de sesión
function handleLogin() {
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

  fetch('/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username, password }),
    credentials: 'include'
  })
  .then(response => {
    if (response.ok) {
      window.location.href = '/users';
    } else {
      throw new Error('Credenciales inválidas');
    }
  })
  .catch(error => {
    alert('Credenciales inválidas');
  });
}

// Función para obtener y mostrar los usuarios en la tabla
function getUsers() {
  fetch('/getUsers', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
    credentials: 'include'
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Error al obtener los usuarios');
    }
    return response.json();
  })
  .then(data => {
    const userListBody = document.querySelector('#user-list tbody');
    userListBody.innerHTML = '';

    data.forEach(user => {
      const row = document.createElement('tr');

      // Nombre
      const nameCell = document.createElement('td');
      nameCell.textContent = user.name;
      row.appendChild(nameCell);

      // Email
      const emailCell = document.createElement('td');
      emailCell.textContent = user.email;
      row.appendChild(emailCell);

      // Usuario
      const usernameCell = document.createElement('td');
      usernameCell.textContent = user.username;
      row.appendChild(usernameCell);

      // Acciones
      const actionsCell = document.createElement('td');

      // Botón de editar
      const editButton = document.createElement('button');
      editButton.textContent = 'Editar';
      editButton.className = 'btn btn-primary mr-2';
      editButton.addEventListener('click', () => {
        editUser(user.id, user.name, user.email, user.username);
      });
      actionsCell.appendChild(editButton);

      // Botón de eliminar
      const deleteButton = document.createElement('button');
      deleteButton.textContent = 'Eliminar';
      deleteButton.className = 'btn btn-danger';
      deleteButton.addEventListener('click', () => {
        deleteUser(user.id);
      });
      actionsCell.appendChild(deleteButton);

      row.appendChild(actionsCell);
      userListBody.appendChild(row);
    });
  })
  .catch(error => {
    alert('No se pudo cargar la lista de usuarios.');
  });
}

// Función para crear un nuevo usuario
function createUser() {
  const data = {
    name: document.getElementById('create-name').value,
    email: document.getElementById('create-email').value,
    username: document.getElementById('create-username').value,
    password: document.getElementById('create-password').value
  };

  fetch('/createUser', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Error al crear el usuario');
    }
    return response.json();
  })
  .then(data => {
    alert('Usuario creado exitosamente.');
    getUsers();
    // Limpiar formulario
    document.getElementById('create-user-form').reset();
  })
  .catch(error => {
    alert('No se pudo crear el usuario.');
  });
}

// Función para cargar los datos del usuario en el formulario de edición
function editUser(id, name, email, username) {
  document.getElementById('edit-user-id').value = id;
  document.getElementById('edit-name').value = name;
  document.getElementById('edit-email').value = email;
  document.getElementById('edit-username').value = username;

  // Mostrar el formulario de edición
  document.getElementById('edit-user-form').style.display = 'block';
}

// Función para actualizar un usuario
function updateUser() {
  const userId = document.getElementById('edit-user-id').value;
  const data = {
    name: document.getElementById('edit-name').value,
    email: document.getElementById('edit-email').value,
    username: document.getElementById('edit-username').value,
    password: document.getElementById('edit-password').value
  };

  fetch(`/updateUser/${userId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data),
    credentials: 'include'
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('No se pudo actualizar el usuario.');
    }
    return response.text();
  })
  .then(data => {
    getUsers();
    document.getElementById('edit-user-form').style.display = 'none';
  })
  .catch(error => {
    alert('No se pudo actualizar el usuario.');
  });
}

// Función para eliminar un usuario
function deleteUser(userId) {
  if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
    fetch(`/deleteUser/${userId}`, {
      method: 'DELETE',
      credentials: 'include'
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('No se pudo eliminar el usuario.');
      }
      alert('Usuario eliminado exitosamente.');
      getUsers();
    })
    .catch(error => {
      alert('No se pudo eliminar el usuario.');
    });
  }
}

// Cargar la lista de usuarios al cargar la página de usuarios
if (window.location.pathname === '/users') {
  getUsers();
}
