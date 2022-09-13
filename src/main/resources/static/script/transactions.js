var app8 = new Vue({
    el: '#app7',
    data: {
        client: [],
        condicion: 0,
        accounts: [],
        numberOrigen: "Selecciona cuenta de origen",
        numberDestino: "Selecciona cuenta de destino",
        numeroDestino: "",
        amount: 0,
        description: "",
        cuentaPropia: true,
        cuentaTercero: false,
        
    },

    created() {
        this.loadData()
    },

    methods: {
        loadData() {
            axios.get("/api/clients/current")
                .then(response => {
                    this.client = response.data;

                    this.accounts = this.client.accounts.filter(account => account.active == true);

                    console.log(this.client)

                    console.log(this.accounts)

                })
                .catch(e => {
                    console.log("error get")

                })
        },
        newtrans(numeroDestino) {
            
            let carga = this.numberOrigen.id
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                  confirmButton: 'btn btn-success',
                  cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
              })
              
              swalWithBootstrapButtons.fire({
                title: 'Estas seguro?',
                text: "Confirma si estas seguro de realizar la transaccion",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Si, enviar!',
                cancelButtonText: 'No, cancelar!',
                reverseButtons: true
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/transaction", "amount=" + this.amount + "&description=" + this.description + "&Destiny=" + numeroDestino + "&Origin=" + this.numberOrigen.number, {
                        headers: { 'content-type': 'application/x-www-form-urlencoded' }
                    })
                    .then(response => {
                        Swal.fire({
                            icon: 'success',
                            title: 'Transferencia exitosa',
                            showConfirmButton: false,
                            timer: 1500,
                    })
                    setTimeout(function(){
                        window.location.href = "/account.html?id="+carga
                    },1500)
                    })
                    .catch(error => {
                        Swal.fire({
                            icon: 'error',
                            title: error.response.data,
                            showConfirmButton: false,
                            timer: 1500
                          })
                    });
                } else if (
                  /* Read more about handling dismissals below */
                  result.dismiss === Swal.DismissReason.cancel
                ) {
                  swalWithBootstrapButtons.fire(
                    'Cancelado',
                    'Se ha cancelado la transaccion',
                    'error'
                  )
                }
              })
            console.log(carga)
            
        },
        cuentaOrigens() {
            this.cuentaTercero = false
            this.cuentaPropia = true
        },
        cuentaDestinos() {
            this.cuentaPropia = false
            this.cuentaTercero = true
        },
        loan(){
            window.location.href = "/loans.html"
        },
        transaction(){
            window.location.href = "/transactions.html"
        },
        
    },



})