var app2 = new Vue({
    el: '#app2',
    data: {
        accounts: [],
        client: [],
        clients:[],
        accountsTrue: [],
        cond: 1,
        condicion: 0,
        account:[],
        transactions: [],
        loans:[],
        currency: "",
        link: {},
        form: {
            email: "",
        },
        cards:[]

    },

    created() {
        const urlParams = new URLSearchParams(window.location.search);
        var myParam = urlParams.get("id");
        this.crearDato()
        this.currency = this.function
        

    },
    methods: {
        crearDato() {
            axios.get("/api/clients/current")
                .then(response => {
                    this.client = response.data;

                    this.accounts = this.client.accounts.filter(cuenta => cuenta.active == true);

                    this.loans = this.client.loans


                    console.log(this.loans);
                    console.log(this.accounts)
                    console.log(this.client)
                })
                .catch(e => {
                    console.log("error get")
                })
                console.log(this.cond);
            axios.get("/api/clients/current/cards")
            .then(res =>{
                this.cards = res.data
                console.log(this.cards);
            })
        },
        
        mostrar(id){
            
            this.account = id
            this.cond = 2
            console.log(this.cond);
            this.transactions = id.transaction
            this.transactions.sort((b, a) => a.date.localeCompare(b.date));
            console.log(this.transactions);
            console.log(this.account);
        },
        function(number){
            return new Intl.NumberFormat('en-US', {style: 'currency',currency: 'USD', minimumFractionDigits: 2}).format(number);
        },
        ChangeClient(client){
            
            Swal.fire({
                title: 'Ingresa tu nuevo email',
                input:"email",
                showCancelButton: true,
                confirmButtonText: 'Save',
            }).then((result) => {
                if (result.isConfirmed) {
                    this.putClient(result.value)
                }
            })
        },
        putClient(mail){

            axios.put(`/api/clients/current`,"email=" + mail,{headers: { 'content-type': 'application/x-www-form-urlencoded'}})
            .then(response => {
                Swal.fire({
                    icon: 'success',
                    title: 'Email actualizado exitosamente',
                    text: 'Sera redirigido a la pagina de ingreso',
                    confirmButtonText: 'Ok',
                  }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = "/index.html"
                    }
                  })
            })         
            
            this.condicion = 0

        },
        logout() {
                Swal.fire({
                    title: 'Desea cerrar su sesion?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: "red",
                    cancelButtonColor: 'gray',
                    confirmButtonText: 'Si, Salir!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post(`/api/logout`)

                        .then(response => {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Has Cerrado Sesion Exitosamente',
                                    showConfirmButton: false,
                                    timer: 1500,
                            })
                        })
                    setTimeout(function(){
                        window.location.href = "/index.html"
                    },1500)
                    }
                })

        },
        createAccount() {
            Swal.fire({
                title: 'Que tipo de cuenta desea crear?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: 'Ahorros',
                denyButtonText: `Corriente`,
              }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        icon: 'question',
                        title: 'Estas seguro de crear una nueva cuenta de ahorros?',
                        showDenyButton: true,
                        showCancelButton: true,
                        confirmButtonText: 'Si!',
                        denyButtonText: `No!`,
                      }).then((result) => {
                        if (result.isConfirmed) {
                            axios.post("/api/clients/current/accounts","type=AHORROS")
                        .then(res => {
                            Swal.fire({
                                icon: 'success',
                                title: 'Cuenta creada exitosamente',
                                confirmButtonText: 'Ok',
                              }).then((result) => {
                                if (result.isConfirmed) {
                                    location.reload()
                                }
                              })
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                              })
                        })
                          
                        } else if (result.isDenied) {
                          Swal.fire('Changes are not saved', '', 'info')
                        }
                      })

                } else if (result.isDenied) {
                    Swal.fire({
                        icon: 'question',
                        title: 'Estas seguro de crear una nueva cuenta corriente?',
                        showDenyButton: true,
                        showCancelButton: true,
                        confirmButtonText: 'Si!',
                        denyButtonText: `No!`,
                      }).then((result) => {
                        if (result.isConfirmed) {
                            axios.post("/api/clients/current/accounts","type=CORRIENTE")
                        .then(res => {
                            Swal.fire({
                                icon: 'success',
                                title: 'Cuenta creada exitosamente',
                                confirmButtonText: 'Ok',
                              }).then((result) => {
                                if (result.isConfirmed) {
                                    location.reload()
                                }
                              })
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                              })
                        })
                          
                        } else if (result.isDenied) {
                          Swal.fire('Changes are not saved', '', 'info')
                        }
                      })

                }
              })
        },
        transaction(){
            window.location.href = "/transactions.html"
        },
        loan(){
            window.location.href = "/loans.html"
        },
        deleteAccount(account){

            Swal.fire({
                icon: 'question',
                title: 'Estas seguro de eliminar esta cuenta?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: 'Si!',
                denyButtonText: `No!`,
              }).then((result) => {
                if (result.isConfirmed) {
                axios.patch("/api/account","numberAccount=" + account.number)
                .then(res => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Cuenta eliminada exitosamente',
                        confirmButtonText: 'Ok',
                      }).then((result) => {
                        if (result.isConfirmed) {
                            location.reload()
                        }
                      })
                })
                .catch(error => {
                    console.log(error);
                })
                  
                } else if (result.isDenied) {
                  Swal.fire('Changes are not saved', '', 'info')
                }
              })


        }
    },
})