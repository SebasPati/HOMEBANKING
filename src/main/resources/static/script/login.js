var app6 = new Vue({
    el: '#app4',
    data: {
        email: "",
        password: "",
        form: {
            firstName: "",
            lastName: "",
            email: "",
            password: "",
        }
    },

    methods: {
        login() {
            axios.
            post('/api/login', "email=" + this.email + "&password=" + this.password, {
                headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => {
                Swal.fire({
                    icon: 'success',
                    title: 'Has ingresado a Home Banking',
                    showConfirmButton: false,
                    timer: 1500
                  })
                  if(this.email == "admin"){
                    setTimeout(function(){
                        window.location.href = "/loanManager.html"
                    },1500)
                  }else{
                    setTimeout(function(){
                        window.location.href = "/accounts.html"
                    },1500)
                    
                  }
                

            }).catch(error => {
                
                Swal.fire({
                    icon: 'error',
                    title: 'usuario o contraseña incorrecta',
                    confirmButtonText: 'Ok',
                  })
            })

        },

    logout() {
        axios.post(`/api/logout`)

        .then(response => console.log('signed out!!'))
            .then

        return (window.location.href = "/index.html")

    },
    AddClient() {
        if (this.form.firstName && this.form.lastName && this.form.email.includes("@")) {
            let client = {
                firstName: this.form.firstName,
                lastName: this.form.lastName,
                email: this.form.email,
                password: this.form.password,
            }
            this.postClient(client)
        }
    },

    postClient(client) {
        console.log(client);
        axios.post('/api/clients', "firstName=" + client.firstName + "&lastName=" + client.lastName + "&email=" + client.email + "&password=" + client.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
            .then(response => {
                console.log('registered')
                Swal.fire({
                    icon: 'success',
                    title: 'Se ha registrado exitosamente',
                    confirmButtonText: 'Ok',
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.
                        post('/api/login', "email=" + client.email + "&password=" + client.password, {
                            headers: {
                                'content-type': 'application/x-www-form-urlencoded'
                            }
                        })
                        .then(response => {
                            return window.location.href = "/accounts.html"
                        })
                    }
                })
            })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: error.response.data,
              })
        });
    }
    }
})
