var app3 = new Vue({
    el: '#app3',
    data: {
        account: {},
        accounts: [],
        client: [],
        clients: [],
        accountsTrue: [],
        condicion: 0,
        account:[],
        transactions: [],
        loans:[],
        currency: "",
        link: {},
        form: {
            email: "",
        },
        
    },
    created() {
        this.loadData()
        this.currency = this.function
        
    },
    methods: {
        loadData() {

            const urlParams = new URLSearchParams(window.location.search);
            const Id = urlParams.get('id');

            axios.get(`/api/accounts/${Id}`)
                .then(response => {
                    this.account = response.data;
                    this.transactions = this.account.transaction.filter(transaction => transaction.active==true)
                    this.transactions = this.transactions.sort((b, a) => a.date.localeCompare(b.date));
                    console.log(response.data);
                })
            axios.get("/api/clients/current")
            .then(response => {
                this.client = response.data;

                this.accounts = this.client.accounts.filter(account => account.active == true);
                
                this.loans = this.client.loans

                console.log(this.loans);
                

                console.log(this.client)
            })
            .catch(e => {
                console.log("error get")
            })
            
        },
        function(number){
            return new Intl.NumberFormat('en-US', {style: 'currency',currency: 'USD', minimumFractionDigits: 2}).format(number);
        },

        ChangeClient(client){
            this.form.email = (client.email)
            this.condicion = 1
            return client
        },
        putClient(client){

            console.log(client);
            
            this.link = client._links.client.href
            console.log(this.link);

            if (this.form.email.includes("@")) {
                  let client = {
                      firstName: this.client.firstName,
                      lastName: this.client.lastName,
                      email: this.form.email,
                  };
                  axios.put(this.link, client)
                  .then(_response => {
                      this.loadData()
                  })
                }
             this.condicion = 0

        },
        loan(){
            window.location.href = "/loans.html"
        },
        transaction(){
            window.location.href = "/transactions.html"
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

    }
})