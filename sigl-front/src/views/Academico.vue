<template>
  <v-data-table
    :headers="titulos"
    :items="alunos"
    :search="search"
    class="elevation-2 data-table"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Gerenciamento de Aluno</v-toolbar-title>
        <v-divider class="mx-4" inset vertical></v-divider>
        <v-text-field
          v-model="search"
          append-icon="mdi-magnify"
          label="Pesquisar"
          single-line
          hide-details
        ></v-text-field>
        <v-spacer></v-spacer>
        
        <vue-select
          @input="redefinirTable" 
          v-model="filtroSelecionado"
          :options="filtros"
          label="Filtro"
        ></vue-select>
        <v-dialog v-model="dialog" max-width="500px">
          <template v-slot:activator="{ on, attrs }" class="template-add">
            <!-- <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">Adicionar</v-btn> -->
            <v-btn
              small
              class="mx-2 add"
              fab
              dark
              color="green"
              v-bind="attrs"
              v-on="on"
              ><v-icon dark> mdi-plus</v-icon></v-btn
            >
          </template>
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ tituloForm }}</span>
            </v-card-title>
          <p v-if="errors.length">
              <b>Por favor, corrija o(s) seguinte(s) erro(s):</b>
              <ul>
                <li v-for="error in errors" :key="error">{{ error }}</li>
              </ul>
            </p>
            <v-card-text>
              <v-form>
                <v-container>
                  <v-row>
                    <v-col cols="8" sm="6" md="4">
                      <v-text-field
                        v-model="itemEditado.pessoa.nome"
                        label="Nome"             
                      ></v-text-field>

                      <v-text-field
                        v-mask="'###.###.###-##'"
                        v-model="itemEditado.pessoa.cpf"
                        label="CPF"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="8" sm="6" md="4">
                      <v-text-field
                        v-model="itemEditado.matricula"
                        label="Matricula"
                        type="number"
                      ></v-text-field>

                      <v-text-field
                        v-model="itemEditado.pessoa.email"
                        label="E-mail"
                        type="email"
                        pattern=".+@globex\.com"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="8" sm="6" md="4">
                      <v-text-field
                        v-model="itemEditado.pessoa.telefone"
                        v-mask="'(##)#####-####'"
                        label="Telefone"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="8" sm="6" md="4"> </v-col>
                    <v-col cols="8" sm="6" md="4"> </v-col>
                  </v-row>
                </v-container>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn small color="warning" dark @click="fechar"> Cancelar</v-btn>
              <v-btn small color="primary" class="mr-4" @click="checkForm">Salvar</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialogDetalhar" max-width="700px">
          <!-- <v-card class="mx-auto"
   > -->
          <v-simple-table dense>
            <template v-slot:default>
              <thead>
                <tr>
                  <th class="align-left">Nome</th>
                  <th class="align-left">CPF</th>
                  <th class="align-left">Telefone</th>
                  <th class="align-left">Email</th>
                  <th class="align-left">Matricula</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>{{ itemEditado.pessoa.nome }}</td>
                  <td>{{ itemEditado.pessoa.cpf }}</td>
                  <td>{{ itemEditado.pessoa.telefone }}</td>
                  <td>{{ itemEditado.pessoa.email }}</td>
                  <td>{{ itemEditado.matricula }}</td>
                </tr>
              </tbody>
            </template>
          </v-simple-table>
        </v-dialog>

        <v-dialog v-model="dialogDesativar" max-width="400px">
          <v-card class="card-modal">
            <v-card-title class="text-h6"
              >Deseja {{ mudarStatus }} este aluno ?</v-card-title
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn small color="warning" dark @click="fecharDesativar"
                >Não</v-btn
              >
              <v-btn small color="primary" dark @click="desativeItemConfirm"
                >Sim</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template v-slot:[`item.acoes`]="{ item }">
      <v-icon small class="mr-2" @click="detalharItem(item)" color="brown"
        >mdi-eye</v-icon
      >
      <v-icon small class="mr-2" @click="editItem(item)" color="blue"
        >mdi-pencil</v-icon
      >
      <v-icon small @click="desativeItem(item)" color="red">
        mdi-power-standby
      </v-icon>
    </template>
  </v-data-table>
</template>

<style>
.add {
  width: 40px;
  height: 40px;
}
.template-add {
  padding-top: 1%;
}
.data-table {
  padding: 3%;
}
</style>

<script>
import Vue from "vue";
import axios from "axios";
import VueAxios from "vue-axios";
Vue.use(VueAxios, axios);

var url = "https://api-sig-itpac-84633.herokuapp.com/api/aluno";
var urlPatch = "https://api-sig-itpac-84633.herokuapp.com/api/aluno/desativar/";
var urlDispatch = "https://api-sig-itpac-84633.herokuapp.com/api/aluno/Ativar/";

export default {
  data: () => ({
    search: "",
    dialog: false,
    dialogDesativar: false,
    dialogDetalhar: false,
    titulos: [
      { text: "Nome", value: "pessoa.nome" },
      { text: "CPF", value: "pessoa.cpf" },
      { text: "Telefone", value: "pessoa.telefone" },
      { text: "Email", value: "pessoa.email" },
      { text: "Status", value: "ativo" },
      { text: "Ações", value: "acoes" },
    ],
    alunos: [],
    errors: [],
    editIndice: -1,
    filtros: ["Ativados", "Todos"],
    filtroSelecionado: "Ativados",
    itemEditado: {
      id: null,
      pessoa: {
        nome: "",
        cpf: "",
        telefone: "",
        email: "",
      },
      matricula: "",
      ativo: null,
    },
    itemPadrao: {
      id: null,
      pessoa: {
        nome: "",
        cpf: "",
        telefone: "",
        email: "",
      },
      matricula: "",
      ativo: true,
    },
  }),
  computed: {
    tituloForm() {
      return this.editIndice === -1 ? "Cadastrar Academico" : "Editar Dados";
    },
    mudarStatus() {
      return this.itemEditado.ativo == "Ativado" ? "desativar " : "ativar ";
    },
  },
  props: {
    pessoa: {
      type: Object,
      default: function () {
        return {
          nome: "",
          cpf: "",
          telefone: "",
          email: "",
        };
      },
    },
  },

  watch: {
    dialog(val) {
      val || this.fechar();
    },
    dialogDesativar(val) {
      val || this.fecharDesativar();
    },
  },

  mounted() {
    this.inicializar();
  },

  methods: {
    inicializar() {
        axios.get(url + "/getAll/true", this.alunos).then((res) => {
          this.alunos = res.data.map((p) => {
            p.ativo = p.ativo ? "Ativado" : "Desativado";
            return p;
          });
          console.log(res.data);
        });
    },

    redefinirTable() {
      if (this.filtroSelecionado === "Todos") {
       this.inicializar();
      } else {
        axios.get(url + "/getAll/true", this.alunos).then((res) => {
          this.alunos = res.data.map((p) => {
            p.ativo = p.ativo ? "Ativado" : "Desativado";
            return p;
          });
          console.log(res.data);
        });
      }
    },

    reloadPage() {
      window.location.reload();
    },

    editItem(item) {
      this.editIndice = this.alunos.indexOf(item);
      this.itemEditado = Object.assign({}, item);
      this.dialog = true;
    },

    desativeItem(item) {
      this.editIndice = this.alunos.indexOf(item);
      this.itemEditado = Object.assign({}, item);
      this.dialogDesativar = true;
    },

    desativeItemConfirm() {
      //this.alunos.splice(this.editIndice, 1);
      if (this.itemEditado.ativo == "Ativado") {
        axios
          .patch(urlPatch + this.itemEditado.id, {
            ativo: false,
          })
          .then((res) => {
            console.log(res.data);
            alert("O acadêmico foi desativado com sucesso !");
            this.reloadPage();
          })
          .catch((error) => {
            console.log(error);
          });
      } else {
        axios
          .patch(urlDispatch + this.itemEditado.id, {
            ativo: true,
          })
          .then((res) => {
            console.log(res.data);
            alert("O acadêmico foi ativado com sucesso !");
            this.reloadPage();
          })
          .catch((error) => {
            console.log(error);
          });
      }
      this.fecharDesativar();
    },

    fechar() {
      this.dialog = false;
      this.$nextTick(() => {
        this.itemEditado = Object.assign({}, this.itemPadrao);
        this.editIndice = -1;
      });
    },

    fecharDesativar() {
      this.dialogDesativar = false;
      this.$nextTick(() => {
        this.itemEditado = Object.assign({}, this.itemPadrao);
        this.editIndice = -1;
      });
    },

    detalharItem(item) {
      this.editIndice = this.alunos.indexOf(item);
      this.itemEditado = Object.assign({}, item);
      var id = this.itemEditado.id;
      axios.get(url + "/" + id).then((res) => {
        this.itemEditado = res.data;
      });

      this.dialogDetalhar = true;
    },

      checkForm() {
      if (this.itemEditado.pessoa.cpf && this.itemEditado.pessoa.nome 
      && this.itemEditado.pessoa.telefone && this.itemEditado.pessoa.email) {
        this.salvar();
        return true;
      }
      this.errors = [];

      if (!this.itemEditado.pessoa.cpf) {
        this.errors.push("O CPF é obrigatório.");
      }
      if (!this.itemEditado.pessoa.nome) {
        this.errors.push("O Nome é obrigatório.");
      }
      if (!this.itemEditado.pessoa.telefone) {
        this.errors.push("O Telefone é obrigatório.");
      }
      if (!this.itemEditado.pessoa.email) {
        this.errors.push("O E-mail é obrigatório.");
      }
    },


    salvar() {
      if (this.editIndice > -1) {
        axios
          .put(url, {
            id: this.itemEditado.id,
            pessoa: {
              id: this.itemEditado.pessoa.id,
              nome: this.itemEditado.pessoa.nome,
              cpf: this.itemEditado.pessoa.cpf,
              email: this.itemEditado.pessoa.email,
              telefone: this.itemEditado.pessoa.telefone,
            },
            matricula: this.itemEditado.matricula,
            ativo: this.itemEditado.ativo === "Ativado",
          })
          .then((res) => {
            alert("Os dados foram atualizados com sucesso !");
            console.log(res.data);
            this.reloadPage();
          })
          .catch((error) => {
            console.log(error);
          });

        Object.assign(this.alunos[this.editIndice], this.itemEditado);
      } else {
        axios
          .post(url, {
            pessoa: {
              nome: this.itemEditado.pessoa.nome,
              cpf: this.itemEditado.pessoa.cpf,
              email: this.itemEditado.pessoa.email,
              telefone: this.itemEditado.pessoa.telefone,
            },
            ativo: true,
            matricula: this.itemEditado.matricula,
          })
          .then((res) => {
            this.alunos = res.data;
            console.log(res.data);
            alert("Os dados foram adicionados com sucesso !");
            this.reloadPage();
          })
          .catch((error) => {
            console.log(error);
          });

        this.alunos.push(this.itemEditado);
      }

      this.fechar();
    },
  },
};
</script>