let app = new Vue({
    el: '#app',
    data: {
        clients: [],
        json: [],
        form: {
            firstName: "",
            lastName: "",
            email: "",
        },
        cont: 0,
        link: [],
        name: "",
        ammount: 0,
        payments: [],
        interest: 0,
        loans: []
        
    },
    created() {
        this.loadData()
        
    },

    methods: {
        loadData() {
            axios.get("api/loans")
            .then(response => {
                this.loans = response.data
            })
            axios.get("rest/clients")
                .then(response => {
                    let data = response.data._embedded.clients;
                    this.json = response.data;
                    this.clients = data;
                })
        },
        AddClient() {
            
            if (this.form.firstName && this.form.lastName && this.form.email.includes("@")) {
                let client = {
                    firstName: this.form.firstName,
                    lastName: this.form.lastName,
                    email: this.form.email,
                };
                this.postClient(client)
            }
        },

        postClient(client) {
            axios.post("rest/clients", client)
                .then(_response => {
                    this.loadData()
                })
        },

        DeleteClient(link) {

             this.link = link._links.client.href
             axios.delete(this.link)
             .then(deleted => { this.loadData() })

        },

        ChangeClient(link){
            this.form.firstName = (link.firstName)
            this.form.lastName = (link.lastName)
            this.form.email = (link.email)
            this.cont = 1
            let boton = document.getElementById(`añadir`)
            boton.className= ("btn btn-danger disabled")

            return link
        },

        putClient(link){

            console.log(link);

            this.link = link._links.client.href
            if (this.form.firstName && this.form.lastName && this.form.email.includes("@")) {
                 let client = {
                     firstName: this.form.firstName,
                     lastName: this.form.lastName,
                     email: this.form.email,
                 };
                 axios.put(this.link, client)
                 .then(_response => {
                     this.loadData()
                 })
            }
            let boton = document.getElementById(`añadir`)
            boton.className=("btn btn-danger")
            this.cont = 0
            this.form.firstName = ""
            this.form.lastName = ""
            this.form.email = ""

        },

        createLoan(){
            console.log(this.name,this.payments,this.ammount,(this.interest/100)+1);
            axios.post("/api/admin/loan", {
                "name": this.name,
                "maxAmmount": this.ammount,
                "payments": this.payments,
                "interest": ((this.interest/100)+1)
            }).then(response =>{
                console.log("ok");
            }).catch(error =>{
                console.log(error);
            })
        }

    }

})