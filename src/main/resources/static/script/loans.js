var app9 = new Vue({
    el: '#app9',
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
        loans:[],
        loansType:[],
        loanName:"",
        loanDues: 0,
        amount: 0,
        payments: [],
        accountNumber: "",
        dues:[],
        balance:[],
        dues:[]
        

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
                    this.accounts = this.client.accounts.filter(account => account.active == true);
                    this.loans = this.client.loans
                    console.log(this.loans);
                    console.log(this.accounts)
                    console.log(this.client)
                })
                .catch(e => {
                    console.log("error get")
                })
                console.log(this.cond);
            axios.get("/api/loans")
            .then(res =>{
                this.loans = res.data
                
                console.log(this.loans);
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
    loan(){
        window.location.href = "/loans.html"
    },
    transaction(){
        window.location.href = "/transactions.html"
    },
        createTransaction(){
            console.log(this.loanName.id)
            console.log(this.amount);
            console.log(this.loanDues);
            console.log(this.accountNumber.number); 
            Swal.fire({
                title: 'Seguro desea solicitar el prestamo?',
                showDenyButton: true,
                showCancelButton: true,
                confirmButtonText: 'Si!',
                denyButtonText: `No!`,
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/loans", {
                        "loanId": this.loanName.id,
                        "amount": this.amount,
                        "payments": this.loanDues,
                        "numberAccount": this.accountNumber.number
                    })
                    .then(response => {
                        Swal.fire({
                            icon: 'success',
                            title: 'Prestamo aceptado y desembolsado',
                            confirmButtonText: 'Ok',
                          }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = "/accounts.html"    
                            }
                          })
                    })
                    .catch(error => {
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: error.response.data,
                            confirmButtonText: 'Vuelve a intentar'
                          })
                    });
                } else if (result.isDenied) {
                  Swal.fire('Changes are not saved', '', 'info')
                }
              })   
            
        },
        transaction(){
            window.location.href = "/transactions.html"
        },
        
    },
})