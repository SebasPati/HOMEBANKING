var app2 = new Vue({
    el: '#app4',
    data: {
        dates:[],
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
        card:[],
        cards:[],
        cartas:[],
        accountNumber:"",
        cardColor: "GOLD",
        cond1: true,
        cond2: false,
        cardType: "DEBITO"
    },

    created() {
        this.crearDato()
        this.currency = this.function
        this.load()
        this.time()

    },
    methods: {
        crearDato() {
        
            axios.get("/api/clients/current")
                .then(response => {
                    this.client = response.data;

                    this.accounts = this.client.accounts.filter(account => account.active == true);

                    this.loans = this.client.loans

                    console.log(this.accounts)

                })
                .catch(e => {
                    console.log("error get")
                })
                console.log(this.cond);
                axios.get("/api/clients/current/cards")
                .then(res =>{
                this.cards = res.data
                this.cards = this.cards.filter(card => card.active == true)
                this.cartas = this.cards.filter(card => card.active == true)
                console.log(this.cartas);

            })
            
                
                
        },

        load(){
            const urlParams = new URLSearchParams(window.location.search);
            const Id = urlParams.get('id');
            if (Id){
                axios.get(`/api/cards/${Id}`)
                .then(response => {
                    this.card = response.data;
                    this.cards = []
                    this.cards.push(this.card)
                })
            }
            
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
        newCard() {
            console.log(this.accountNumber.number, this.cardColor);
            let number = this.accountNumber.number
            axios.post("/api/clients/current/debitCards ", "cardColor=" + this.cardColor + "&accountNumber=" + number, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Tarjeta añadida con exito',
                        confirmButtonText: 'Ok',
                      }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/cards.html"
                        }
                      })
                      
                    console.log("CREADO")
                    
                })
                .catch(error => {
                    if(number == undefined){
                        console.log("hola");
                        Swal.fire({
                            icon: 'error',
                            title: 'Por favor selecciona una cuenta',
                            confirmButtonText: 'Ok',
                          })
                    }else{
                        Swal.fire({
                            icon: 'error',
                            title: error.response.data,
                            confirmButtonText: 'Ok',
                          })
                    }
                });
        },
        time(card){
            date = new Date();
            year = date.getFullYear();
            month = date.getMonth() + 1;
            day = date.getDate();
            this.dates = year + "-" + month + "-" + day;
            console.log(this.dates);
            console.log("2022-09-10">this.dates);
            
        },
        newCreditCard() {
            axios.post("/api/clients/current/creditCards ", "cardColor=" + this.cardColor, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => {
                    Swal.fire({
                        icon: 'success',
                        title: 'Tarjeta añadida con exito',
                        confirmButtonText: 'Ok',
                      }).then((result) => {
                        if (result.isConfirmed) {
                            window.location.href = "/cards.html"
                        }
                      })
                      
                    console.log("CREADO")
                    
                })
                .catch(error => {
                    console.log(error);     
                    Swal.fire({
                        icon: 'error',
                        title: error.response.data,
                        confirmButtonText: 'Ok',
                      })
                });
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
    loan(){
        window.location.href = "/loans.html"
    },
    transaction(){
        window.location.href = "/transactions.html"
    },
    deleteCard(carta){
        let numero = carta.number
        console.log(numero);
        Swal.fire({
            title: 'Seguro desea eliminar esta tarjeta?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: "red",
            cancelButtonColor: 'gray',
            confirmButtonText: 'Si, eliminar!'
        }).then((result) => {
            if (result.isConfirmed) {
                axios.patch(`/api/card`, "cardNumber=" + numero)
                .then(response => {
                        Swal.fire({
                            icon: 'success',
                            title: 'Se ha eliminado la tarjeta exitosamente',
                            showConfirmButton: false,
                            timer: 1500,
                    })
                .catch(error =>{
                    console.log(error);
                })
                })
            setTimeout(function(){
                location.reload();
            },1500)
            }
        })
    
    },
    },
    
})